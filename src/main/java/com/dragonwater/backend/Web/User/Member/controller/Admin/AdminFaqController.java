package com.dragonwater.backend.Web.User.Member.controller.Admin;


import com.dragonwater.backend.Web.Support.FAQ.domain.FAQs;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqCategoryReqDto;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqReqDto;
import com.dragonwater.backend.Web.Support.FAQ.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminFaqController {

    // 인터페이스 구현 완
    private final FaqService faqService;

    // Faq  카테고리 추가하기
    @PostMapping("/faq/categories")
    public ResponseEntity<?> addCategories(@RequestBody FaqCategoryReqDto dto) {
        log.info("카테고리 추가");
        faqService.addCategory(dto);
        return ResponseEntity.ok(HttpStatus.SC_CREATED);
    }

    // Faq 카테고리 가져오기
    @GetMapping("/faq/categories")
    public ResponseEntity<?> allCategories() {
        return ResponseEntity.ok(faqService.getAllCategories());
    }

    // Faq 추가하기
    @PostMapping("/faq/add")
    public ResponseEntity<?> addFaq(@RequestBody FaqReqDto dto) {
        FAQs faQs = faqService.addFAQ(dto);
        return ResponseEntity.ok(faQs);
    }

    // 카테고리 별로 FAQ 가져오기
    @GetMapping("/faq")
    public ResponseEntity<?> getFaqs(@RequestParam Long categoryId) {
        return ResponseEntity.ok(faqService.getFaqByCategory(categoryId));
    }

    @PutMapping("/faq/{faqId}")
    public ResponseEntity<?> editFaq(@RequestBody FaqReqDto dto, @PathVariable Long faqId) {
        FAQs faQs = faqService.editFAQ(dto, faqId);
        return ResponseEntity.ok(faQs);
    }
}
