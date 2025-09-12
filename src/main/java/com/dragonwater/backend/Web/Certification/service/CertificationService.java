package com.dragonwater.backend.Web.Certification.service;

import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface CertificationService {


    /**
     * 이메일 인증을 위해서 메일로 인증번호를 보내주는 메소드
     * @param email 수신 메일 주소
     * @param authCode 인증번호
     */
    void sendVerificationEmail(String email, String authCode);


    /**
     * 인증을 위해서 인증번호를 핸드폰으로 보내주는 메소드
     * @param receiverNumber 수신인 번호
     * @param verifyCode 인증번호
     * @throws JsonProcessingException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    void sendVerificationPhone(String receiverNumber, String verifyCode) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;

    /**
     * 초기화된 비밀번호 알려주는 메소드
     * @param member 멤버 객체
     * @param tempPassword 초기화된 비밀번호
     */
    void sendResetPassword(Members member, String tempPassword);

    /**
     * 아이디를 다시 보내주는 코드
     * @param member 멤버 객체
     */
    void sendLoginId(Members member);

    /**
     * 아이디/비밀번호 찾기 관련 핸드폰 인증
     * @param receiverNumber 수신 번호
     * @param verifyCode 인증번호
     */
    void sendLoginVerifyPhone(String receiverNumber, String verifyCode) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

    /**
     * 아이디/비밀번호 찾기 관련 이메일 인증
     * @param email 수신 이메일
     * @param verifyCode 인증번호
     */
    void sendLoginVerifyEmail(String email, String verifyCode);
}
