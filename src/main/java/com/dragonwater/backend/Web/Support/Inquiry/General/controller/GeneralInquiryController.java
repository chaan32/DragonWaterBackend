package com.dragonwater.backend.Web.Support.Inquiry.General.controller;

import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Web.Support.Claim.service.ClaimService;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.General.service.GeneralInquiryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
public class GeneralInquiryController {
    // 인터페이스 구현 완
    private final ClaimService claimService;
    // 인터페이스 구현 미완


    private final GeneralInquiryService inquiryService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addGInquiry(@RequestPart("data") GeneralInquiryReqDto dto,
                                         @RequestPart(value = "attachments", required = false) List<MultipartFile> files,
                                         HttpServletRequest request) throws IOException {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));

        String category = dto.getCategory();

        if ( category.equals("exchange") || category.equals("refund")){
            claimService.addClaims(dto, files, memberId);
        }
        else {
            inquiryService.addGeneralInquiry(dto, memberId, files);
        }

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addGInquiryWithJson(@RequestBody GeneralInquiryReqDto dto,
                                                 HttpServletRequest request) throws IOException {

        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));

        // 파일이 없는 JSON 요청을 처리하는 로직
        String token = request.getHeader("Authorization").substring(7);

        String category = dto.getCategory();log.info("add GI dto : {}", token);
        log.info("category : {}", category);
        if ( category.equals("exchange") || category.equals("refund")){
            claimService.addClaims(dto, null, memberId);
        }
        else {
            inquiryService.addGeneralInquiry(dto, memberId, null);
        }

        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}

