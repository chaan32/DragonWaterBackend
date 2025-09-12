package com.dragonwater.backend.Web.Certification.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.SendSMSFailedException;
import com.dragonwater.backend.Web.Certification.dto.NaverSmsReqDto;
import com.dragonwater.backend.Web.Certification.dto.NaverSmsResDto;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {
    private final JavaMailSender javaMailSender;
    private final WebClient smsApiClient;
    private final SpringTemplateEngine templateEngine;



    @Value("${naver.access-key}")
    private String accessKey;

    @Value("${naver.secret-key}")
    private String secretKey;

    @Value("${naver.sms.server-id}")
    private  String serverId;

    @Value("${naver.sms.phone}")
    private String senderNumber;

    @Override
    @Async
    public void sendVerificationEmail(String email, String authCode) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();

        Context context = new Context();
        context.setVariable("authCode", authCode);
        String htmlMessage = templateEngine.process("emailAuth", context);


        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[Dragon Water] 이메일 인증 번호를 확인해주세요.");
            helper.setText(htmlMessage, true);
            javaMailSender.send(mimeMailMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송에 실패했습니다.", e);
        }
    }

    @Override
    @Async
    public void sendVerificationPhone(String receiverNumber, String verifyCode) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("수신인 : {} , 인증번호 : {}",receiverNumber, verifyCode);

        final String smsURL = "https://sens.apigw.ntruss.com/sms/v2/services/"+serverId+"/messages";

        final String time = String.valueOf(System.currentTimeMillis());

        final NaverSmsReqDto messageDto = NaverSmsReqDto.of(senderNumber, receiverNumber, verifyCode);
        final String body = new ObjectMapper().writeValueAsString(messageDto);
        String signatureKey = makeSignature(time);

        // .block()을 제거하고, subscribe()를 통해 논블로킹 방식으로 호출
        smsApiClient.post().uri(smsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-ncp-apigw-timestamp", time)
                .header("x-ncp-iam-access-key", this.accessKey)
                .header("x-ncp-apigw-signature-v2", signatureKey)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(NaverSmsResDto.class)
                .doOnSuccess(resDto -> {
                    log.info("SENS API 응답 성공");
                })
                // 에러 발생 시, 에러 응답을 로그로 기록
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("---[SENS API 에러 응답]---");
                    log.error("HTTP Status Code: {}", e.getStatusCode());
                    log.error("Error Response Body: {}", e.getResponseBodyAsString());
                    log.error("--------------------------");
                })
                .subscribe();
    }


    @Override
    @Async
    public void sendResetPassword(Members member, String tempPassword) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();

        Context context = new Context();
        context.setVariable("password", tempPassword);
        String name = member.getName();
        if (member instanceof BranchMembers) {
            BranchMembers member1 = (BranchMembers) member;
            name += "-" + member1.getBranchName();
        }
        context.setVariable("name", name);
        String htmlMessage = templateEngine.process("resetPassword", context);


        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");
            helper.setTo(member.getEmail());
            helper.setSubject("[Dragon Water] 비밀번호 복구가 완료되었습니다.");
            helper.setText(htmlMessage, true);
            javaMailSender.send(mimeMailMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송에 실패했습니다.", e);
        }
    }

    @Override
    @Async
    public void sendLoginId(Members member) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();

        Context context = new Context();
        String name = member.getName();
        if (member instanceof BranchMembers) {
            BranchMembers member1 = (BranchMembers) member;
            name += "-" + member1.getBranchName();
        }
        context.setVariable("name", name);
        context.setVariable("loginId", member.getLoginId());
        String htmlMessage = templateEngine.process("findId", context);

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");
            helper.setTo(member.getEmail());
            helper.setSubject("[Dragon Water] 회원 계정 로그인 계정 찾기 완료");
            helper.setText(htmlMessage, true);
            javaMailSender.send(mimeMailMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송에 실패했습니다.", e);
        }
    }

    @Override
    public void sendLoginVerifyPhone(String receiverNumber, String verifyCode) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        log.info("로그인 관련 인증 - 수신인 : {} , 인증번호 : {}",receiverNumber, verifyCode);

        final String smsURL = "https://sens.apigw.ntruss.com/sms/v2/services/"+serverId+"/messages";

        final String time = String.valueOf(System.currentTimeMillis());

        final NaverSmsReqDto messageDto = NaverSmsReqDto.certify(senderNumber, receiverNumber, verifyCode);
        final String body = new ObjectMapper().writeValueAsString(messageDto);
        String signatureKey = makeSignature(time);

        // .block()을 제거하고, subscribe()를 통해 논블로킹 방식으로 호출
        smsApiClient.post().uri(smsURL)
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-ncp-apigw-timestamp", time)
                .header("x-ncp-iam-access-key", this.accessKey)
                .header("x-ncp-apigw-signature-v2", signatureKey)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(NaverSmsResDto.class)
                .doOnSuccess(resDto -> {
                    log.info("SENS API 응답 성공");
                    log.info("로그인 id/pw 관련 핸드폰 인증");
                })
                // 에러 발생 시, 에러 응답을 로그로 기록
                .doOnError(WebClientResponseException.class, e -> {
                    log.error("---[SENS API 에러 응답]---");
                    log.error("HTTP Status Code: {}", e.getStatusCode());
                    log.error("Error Response Body: {}", e.getResponseBodyAsString());
                    log.error("--------------------------");
                })
                .subscribe();
    }

    @Override
    public void sendLoginVerifyEmail(String email, String verifyCode) {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();

        Context context = new Context();
        context.setVariable("verifyCode", verifyCode);
        String htmlMessage = templateEngine.process("verify", context);


        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, false, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[Dragon Water] 로그인 정보 찾기 - 인증번호 발송");
            helper.setText(htmlMessage, true);
            javaMailSender.send(mimeMailMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송에 실패했습니다.", e);
        }
    }

    private String makeSignature(String currentTime) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";					// one space
        String newLine = "\n";					// new line
        String method = "POST";					// method
        String url = "/sms/v2/services/"+serverId+"/messages";	// url (include query string)
        String timestamp = currentTime;	// current timestamp (epoch)
        String accessKey = this.accessKey;			// access key id (from portal or Sub Account)
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);

        return encodeBase64String;
    }
}
