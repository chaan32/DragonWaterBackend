package com.dragonwater.backend.Config.Jwt;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.MemberNotFoundException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Token.AuthenticationRequiredException;
import com.dragonwater.backend.Config.Redis.RedisDao;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import com.dragonwater.backend.Web.User.Member.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final RedisDao redisDao;

    // 기본 토큰 만료 시간
    private static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(5);
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(10);
    private final MemberRepository memberRepository;




    // Member 객체로 AccessToken, RefreshToken 둘 다 생성
    public ConcurrentHashMap<String, String> generateAccessTokenAndRefreshToken(Members member) {
        // accessToken, refreshToken 만들기
        String accessToken = makeAccessToken(member);
        String refreshToken = makeRefreshToken(member);

        // 보내줄 HashMap에 넣기
        ConcurrentHashMap<String, String> tokenPair = new ConcurrentHashMap<>();
        tokenPair.put("accessToken", accessToken);
        tokenPair.put("refreshToken", refreshToken);

        //레디스 캐시 메모리에 refreshToken을 넣기
        redisDao.setValues(member.getId().toString(), refreshToken, REFRESH_TOKEN_DURATION);
        return tokenPair;
    }





    /**
     *     [ 생성 메소드 ]
     *
     *          refreshAccessToken (refreshToken) : refreshToken을 통해서 accessToken 새로 생성하기
     *          makeRefreshToken (Members) : members 객체를 통해서 refreshToken 만들기 이메일 정보만 있다
     *          makeAccessToken (Members) : members 객체를 통해서 accessToken 만들기 (id, email, role)
     */

    public ConcurrentHashMap<String, String> reIssuedAccessToken(String refreshToken) {

        // 토큰 자체가 유효한 토큰인지 = 우리가 발급한 토큰인지
        if (!validToken(refreshToken)) {
            throw new AuthenticationRequiredException();
        }

        // 현재 토큰의 Subject는 Members의 id 식별값
        String subject = getSubjectFromRefreshToken(refreshToken);

        // 레디스에 저장된 거랑 비교 해
        String storedToken = (String) redisDao.getValues(subject);
        redisDao.deleteValues(subject);


        if (!storedToken.equals(refreshToken)) {
            throw new AuthenticationRequiredException();
        }

        // userId를 통해서 멤버 정보 다시 가져와야 함
        Members member = memberRepository.findById(Long.parseLong(subject))
                .orElseThrow(()-> new MemberNotFoundException("해당 회원를 찾을 수 없습니다."));


        return generateAccessTokenAndRefreshToken(member);

    }
    private String makeRefreshToken(Members member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_DURATION.toMillis());
        try {
            return Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setSubject(member.getId().toString())
                    .setExpiration(expiration)
                    .setIssuedAt(now)
                    .setIssuer(jwtProperties.getIssuer())
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("토큰 생성 실패", e);
        }
    }
    private String makeAccessToken(Members member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_DURATION.toMillis());
        try {
            String name = member.getName();
            if (member.getRole() == Role.BRANCH) {
                BranchMembers bm = (BranchMembers) member;
                name += " - "+ bm.getBranchName();
            }
            log.info("");
            return Jwts.builder()
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setSubject(member.getId().toString())
                    .setExpiration(expiration)
                    .setIssuedAt(now)
                    .setIssuer(jwtProperties.getIssuer())
                    .claim("id", member.getId())
                    .claim("role", member.getRole().name())
                    .claim("username", name)
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("토큰 생성 실패", e);
        }
    }




    /**
     *     [ 추출 메소드 ]
     *          getMemberRole(token) : 멤버의 role 얻기
     *          getMemberId (token) : 멤버의 Id 값 얻기
     *          getAuthentication (token) : Authentication 객체 얻기
     *          getClaims (token) : 클레임 객체 얻기
     */
    public Role getMemberRole(String token) {
        try {
            Claims claims = getClaims(token);
            String roleName = claims.get("role", String.class);

            if (roleName == null) {
                throw new RuntimeException("토큰에 role 정보가 없습니다.");
            }

            return Role.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            log.error("올바르지 않은 Role 값: {}", e.getMessage());
            throw new RuntimeException("올바르지 않은 Role 값", e);
        } catch (Exception e) {
            log.error("멤버 Role 추출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("멤버 Role 추출 실패", e);
        }
    }
    public String getSubjectFromRefreshToken(String refreshToken) {
        Claims claims = getClaims(refreshToken);
        return claims.getSubject();
    }
    public Long getMemberId(String token) {
        try {

            Claims claims = getClaims(token);
            Object idClaim = claims.get("id");


            if (idClaim == null) {
                throw new RuntimeException("토큰에 ID 정보가 없습니다.");
            }

            if (idClaim instanceof Integer) {
                log.info("id 값 Long으로 변환 함");
                return ((Integer) idClaim).longValue();
            } else if (idClaim instanceof Long) {
                return (Long) idClaim;
            } else if (idClaim instanceof String) {
                return Long.parseLong((String) idClaim);
            } else if (idClaim instanceof Number) {
                return ((Number) idClaim).longValue();
            } else {
                throw new RuntimeException("ID claim 타입이 올바르지 않습니다: " + idClaim.getClass().getName());
            }
        } catch (Exception e) {
            log.error("멤버 ID 추출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("멤버 ID 추출 실패", e);
        }
    }
    public Authentication getAuthentication(String token) {
        try {
            if (!validToken(token)) {
                log.warn("유효하지 않은 토큰으로 인증을 시도했습니다.");
                return null;
            }

            Claims claims = getClaims(token);
            Role memberRole = getMemberRole(token);
            Set<SimpleGrantedAuthority> authorities =
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));

            return new UsernamePasswordAuthenticationToken(
                    new User(claims.getSubject(), "", authorities),
                    token,
                    authorities
            );
        } catch (Exception e) {
            log.error("토큰으로 인증 생성 실패: {}", e.getMessage(), e);
            return null;
        }
    }
    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Claims 파싱 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Claims 파싱 실패", e);
        }
    }



    /**
     *     [ 검증 메소드 ]
     *          validToken(token) : 토큰의 유효성 검증
     *          isValidJwtFormat : JWT 기본 형식 검증
     *          isBase64UrlSafe : Base64 URL safe 검증
     *          validRefreshToken (token) : refresh token이 유효 한지?
     */
    public boolean validToken(String token) {

        if (token == null || token.trim().isEmpty()) {
            log.debug("토큰이 null이거나 비어있습니다.");
            return false;
        }

        // JWT 기본 형식 검증 먼저 수행
        if (!isValidJwtFormat(token)) {
            log.warn("JWT 형식이 올바르지 않습니다: {}",
                    token.length() > 50 ? token.substring(0, 50) + "..." : token);
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰입니다: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 토큰입니다: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("잘못된 형식의 토큰입니다: {}", e.getMessage());
            return false;
        } catch (SignatureException e) {
            log.warn("잘못된 서명의 토큰입니다: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("잘못된 토큰입니다: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("토큰 검증 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }






    private boolean isValidJwtFormat(String token) {
        if (token == null) return false;

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            log.debug("JWT는 3개 부분으로 구성되어야 하지만 {}개 부분이 발견됨", parts.length);
            return false;
        }

        for (int i = 0; i < parts.length; i++) {
            if (!isBase64UrlSafe(parts[i])) {
                log.debug("JWT의 {}번째 부분이 Base64URL 형식이 아닙니다", i + 1);
                return false;
            }
        }
        return true;
    }
    private boolean isBase64UrlSafe(String str) {
        if (str == null || str.isEmpty()) return false;
        return str.matches("^[A-Za-z0-9_-]+$");
    }


    // 서명 키 반환 메소드
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }
}