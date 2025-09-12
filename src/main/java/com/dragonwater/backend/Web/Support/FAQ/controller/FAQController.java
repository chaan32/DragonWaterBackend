package com.dragonwater.backend.Web.Support.FAQ.controller;

import com.dragonwater.backend.Web.Support.FAQ.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
@Slf4j
public class FAQController {
    // 인터페이스 구현 완
    private final FaqService faqService;

    // 카테고리 가져오기
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(faqService.getAllCategories());
    }

    // 카테고리 별로 FAQ 가져오기
    @GetMapping
    public ResponseEntity<?> getFaqs(@RequestParam Long categoryId) {
        return ResponseEntity.ok(faqService.getFaqByCategory(categoryId));
    }
}
