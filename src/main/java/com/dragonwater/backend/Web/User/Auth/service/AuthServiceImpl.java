package com.dragonwater.backend.Web.User.Auth.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC.ApprovalPendingException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.LoginFailedException;
import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Config.Redis.RedisDao;
import com.dragonwater.backend.Web.User.Auth.dto.Login.LoginSuccessTokensDto;
import com.dragonwater.backend.Web.User.Auth.dto.Login.LoginRequestDto;
import com.dragonwater.backend.Web.User.Auth.dto.Login.LoginSuccessResDto;
import com.dragonwater.backend.Web.User.Auth.dto.Login.UpdatePwReqDto;
import com.dragonwater.backend.Web.User.Member.domain.*;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService{

    // 인터페이스 구현 완
    private final MemberService memberService;

    // 인터페이스 구현 미완

    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisDao redisDao;


    public LoginSuccessTokensDto loginProcess(LoginRequestDto dto) {

        String loginId = dto.getUsername();
        String loginPw = dto.getPassword();

        Members member = memberService.getMemberByLoginId(loginId);

        if (!encoder.matches(loginPw, member.getLoginPw())) {
            throw new LoginFailedException();
        }

        // 회원의 승인 여부 : 법인이 아닌 경우에만 체크하며 승인이 되지 않은 경우는 예외를 터트림
        validationApproveStatus(member);

        // 토큰 받아오기
        ConcurrentHashMap<String, String> map = jwtTokenProvider.generateAccessTokenAndRefreshToken(member);
        String accessToken = map.get("accessToken");
        String refreshToken = map.get("refreshToken");

        // 정상적인 토큰인지 검증
        jwtTokenProvider.validToken(accessToken);
        jwtTokenProvider.validToken(refreshToken);

        return LoginSuccessTokensDto.of(accessToken, refreshToken);
    }

    public boolean passwordReCheck(LoginRequestDto dto, Long memberId) {
        String password = dto.getPassword();
        Members members = memberService.getMemberById(memberId);
        if (encoder.matches(password, members.getLoginPw())) {
            return true;
        }
        return false;
    }

    // 비밀번호 초기화
    @Transactional
    public boolean updatePw(UpdatePwReqDto pwReqDto, Long memberId) {
        String password = pwReqDto.getNewPassword();
        Members members = memberService.getMemberById(memberId);
        String encodedPw = encoder.encode(password);
        members.updatePassword(encodedPw);
        return encoder.matches(password, members.getLoginPw());
    }

    @Override
    public void logout(Long memberId) {
        log.info("logout memberId : {}", memberId);
        Object values = redisDao.getValues(String.valueOf(memberId));
        log.info("token : {}", values);
        redisDao.deleteValues(String.valueOf(memberId));
    }

    // 법인 회원의 경우 승인이 됐는지 확인하기
    private void validationApproveStatus(Members member) {
        if (member instanceof BranchMembers) {
            BranchMembers branchMembers = (BranchMembers) member;
            if (branchMembers.getApprovalStatus() == ApprovalStatus.PENDING) {
                throw new ApprovalPendingException();
            }
        }

        if (member instanceof HeadQuarterMembers) {
            HeadQuarterMembers headQuarterMembers = (HeadQuarterMembers) member;
            if (headQuarterMembers.getApprovalStatus() == ApprovalStatus.PENDING) {
                throw new ApprovalPendingException();
            }
        }
    }

    // 로그인 성공 리스폰스 객체 바로 받아오기
    private LoginSuccessResDto createLoginResponse(Members member) {
        return switch (member.getRole()) {
            case HEADQUARTERS -> LoginSuccessResDto.of((HeadQuarterMembers) member);
            case BRANCH -> LoginSuccessResDto.of((BranchMembers) member);
            case INDIVIDUAL -> LoginSuccessResDto.of((IndividualMembers) member);
            case ADMIN -> LoginSuccessResDto.of((AdminMembers) member);
            case CORPORATE -> null;
        };
    }
}
