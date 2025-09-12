package com.dragonwater.backend.Web.User.Member.controller.Admin;


import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryResDto;
import com.dragonwater.backend.Web.Support.Inquiry.General.service.GeneralInquiryService;
import com.dragonwater.backend.Web.Support.Inquiry.General.service.GeneralInquiryServiceImpl;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminAnswerReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminSIQnAResDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.service.SpecificInquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Queue;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminInquiriesController {

    // 인터페이스 구현 완
    private final SpecificInquiryService SIInquiryService;
    private final GeneralInquiryService GRInquiryService;

    // 인터페이스 구현 미완


    // 제품 문의 목록 가져오기 (답변 대기 중인 상태의 문의)
    @GetMapping("/product-inquiries")
    public ResponseEntity<?> getNonAnsweredQuestion(@RequestParam("answered") Boolean answered) {
        Queue<AdminSIQnAResDto> dtos = SIInquiryService.getInquiresAtAdminPanelByAnswered(answered);
        return ResponseEntity.ok(dtos);
    }


    // 제품 별 문의 목록 가져오기 (답변을 하든 안 했든 상관 X)
    @GetMapping("/product-inquiries/{productId}")
    public ResponseEntity<?> getProductInquiries(@PathVariable Long productId) {
        Queue<AdminSIQnAResDto> dtos = SIInquiryService.getInquiresAtAdminPanelByProductId(productId);
        return ResponseEntity.ok(dtos);
    }


    // 제품 문의 답변 등록하기
    @PostMapping("/product-inquiries/answer")
    public ResponseEntity<?> uploadProductAnswer(@RequestBody AdminAnswerReqDto dto) {
        SIInquiryService.uploadAnswer(dto);
        return ResponseEntity.ok("답변이 성공적으로 등록되었습니다.");
    }

    // 일반 문의 목록 가져오기
    @GetMapping("/inquiries")
    public ResponseEntity<?> getGeneralQuestion(@RequestParam("answered") Boolean answered) {
        Queue<GeneralInquiryResDto> generalInquires = GRInquiryService.getGeneralInquires(answered);
        for (GeneralInquiryResDto generalInquire : generalInquires) {
            log.info("id : {} ", generalInquire.getInquiryId());
        }
        return ResponseEntity.ok(generalInquires);
    }

    // 일반 문의 답변 등록하기
    @PostMapping("/inquiries/answer")
    public ResponseEntity<?> uploadGeneralAnswer(@RequestBody AdminAnswerReqDto dto) {
        GRInquiryService.uploadAnswer(dto);
        return ResponseEntity.ok("답변이 성공적으로 등록되었습니다.");
    }


}
