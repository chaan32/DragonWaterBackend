package com.dragonwater.backend.Web.Support.Inquiry.controller;

import com.dragonwater.backend.Web.Support.Inquiry.dto.InquiriesResDto;
import com.dragonwater.backend.Web.Support.Inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/inquiries")
@Slf4j
public class InquiryController {
    // 인터페이스 구현 완
    private final InquiryService inquiryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getInqs(@PathVariable Long id) {
        InquiriesResDto membersInquiries = inquiryService.getMembersInquiries(id);

        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("data", membersInquiries);
        return ResponseEntity.ok(response);
    }
}
