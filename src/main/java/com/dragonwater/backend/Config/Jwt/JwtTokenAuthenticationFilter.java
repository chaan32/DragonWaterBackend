package com.dragonwater.backend.Config.Jwt;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.MemberNotFoundException;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final static String HEADER_AUTH = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청에서 헤더 꺼내기
        String authorizationHeader = request.getHeader(HEADER_AUTH);


        // 헤더에서 토큰 꺼내기
        String accessToken = getAccessToken(authorizationHeader);



        // 토큰이 유효 토큰인지 체크
        if (accessToken != null && jwtTokenProvider.validToken(accessToken)) {
            try {
                // 토큰에서 id 꺼내기
                Long memberId = jwtTokenProvider.getMemberId(accessToken);

                // 멤버 찾기
                Members member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new MemberNotFoundException(memberId));

                // Authentication 객체 생성
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole().name());

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        member,
                        null,
                        Collections.singletonList(authority)
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.error("토큰 처리 중 오류 발생: {}", e.getMessage(), e);
                SecurityContextHolder.clearContext();
            }
        } else {
            SecurityContextHolder.clearContext();
            log.debug("토큰이 없거나 유효하지 않음");
        }

        filterChain.doFilter(request, response);
    }
    private String getAccessToken(String authorizationHeader) {
        // null 체크
        if (authorizationHeader == null) {
            log.debug("Authorization 헤더가 null입니다.");
            return null;
        }

        // Bearer 접두사 체크 및 제거
        if (authorizationHeader.startsWith(TOKEN_PREFIX)) {
            String token = authorizationHeader.substring(TOKEN_PREFIX.length()).trim();
            log.debug("Bearer 접두사 제거 후 토큰 길이: {}", token.length());
            return token;
        }

        log.debug("Authorization 헤더가 Bearer로 시작하지 않습니다: {}", authorizationHeader);
        return null;
    }
}
