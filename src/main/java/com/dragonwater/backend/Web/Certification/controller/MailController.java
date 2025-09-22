package com.dragonwater.backend.Web.Mail.controller;

import com.dragonwater.backend.Web.Mail.service.MailService;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@Slf4j
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final MemberService memberService;

    @GetMapping("/send")
    public void sendMailTest() throws Exception {
        mailService.sendSimpleMailMessage("이건 컨트롤러에서 보낸 메일임");
    }

    @PostMapping("/send/{userId}")
    public String sendMailWithUserId(@PathVariable Long userId) {
        try {
            Members member = memberService.getMemberById(userId);
            mailService.sendMimeMessage(member);
            return "메일 발송 성공";
        } catch (Exception e) {
            return "메일 발송 실패";
        }
    }
}
