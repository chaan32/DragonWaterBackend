package com.dragonwater.backend.Web.Certification.dto;


import com.dragonwater.backend.Web.User.Member.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
public class NaverSmsReqDto {
    private String type;
    private String from;
    private String content;
    private String contentType;
    private String countryCode;
    private List<NaverMessageDto> messages;

    public static NaverSmsReqDto of(String senderNumber, String receiverNumber, String verifyCode) {
        List<NaverMessageDto> messages = new LinkedList<>();
        messages.add(new NaverMessageDto(receiverNumber));
        return NaverSmsReqDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderNumber)
                .content("[DRAGON WATER]\n 본인확인 인증번호 ["+verifyCode+"] 타인 노출 금지!!")
                .messages(messages)
                .build();
    }

    public static NaverSmsReqDto sendOrder(String senderNumber, String receiverNumber, String productName) {
        List<NaverMessageDto> messages = new LinkedList<>();
        messages.add(new NaverMessageDto(receiverNumber));
        return NaverSmsReqDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderNumber)
                .content("[DRAGON WATER]\n 주문 상품 : "+productName+"\n 주문이 완료 되었습니다. 감사합니다.")
                .messages(messages)
                .build();
    }

    public static NaverSmsReqDto notifyToAdmin(String senderNumber, String receiverNumber, Role role) {
        List<NaverMessageDto> messages = new LinkedList<>();
        messages.add(new NaverMessageDto(receiverNumber));
        return NaverSmsReqDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderNumber)
                .content("[DRAGON WATER]\n"+role.getRole()+" 주문이 들어왔습니다.")
                .messages(messages)
                .build();
    }

    public static NaverSmsReqDto certify(String senderNumber, String receiverNumber, String verifyCode) {
        List<NaverMessageDto> messages = new LinkedList<>();
        messages.add(new NaverMessageDto(receiverNumber));
        return NaverSmsReqDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(senderNumber)
                .content("[DRAGON WATER]\n -아이디 혹은 비밀번호 찾기- \n 본인확인 인증번호 ["+verifyCode+"] 타인 노출 금지!!")
                .messages(messages)
                .build();
    }
}
