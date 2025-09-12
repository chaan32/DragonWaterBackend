package com.dragonwater.backend.Web.User.Auth.service;

import com.dragonwater.backend.Web.User.Auth.dto.Login.*;

public interface AuthService {

    /**
     *  로그인 프로세스
     * @param dto 1) username : 아이디, 2) password : 비밀번호
     * @return accessToken, refreshToken
     */
    LoginSuccessTokensDto loginProcess(LoginRequestDto dto);

    /**
     *  비밀번호 변경 시 비밀번호로 유저를 다시 한번 체크하는 메소드
     * @param dto 1) username : 아이디, 2) password : 비밀번호
     * @param memberId 요청하는 사람의 식별 memberId
     * @return
     */
    boolean passwordReCheck(LoginRequestDto dto, Long memberId);


    /**
     * 비빌번호를 변경하는 메소드
     * @param pwReqDto newPassword : 새로운 비밀번호
     * @param memberId 요청하는 사람의 식별 memberId
     * @return
     */
    boolean updatePw(UpdatePwReqDto pwReqDto, Long memberId);

    /**
     * 로그아웃 메소드 -> redis에서 저장한 거 삭제해야 함
     * @param memberId
     */
    void logout(Long memberId);
}
