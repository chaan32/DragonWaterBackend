package com.dragonwater.backend.Web.Support.Notice.contoller;

import com.dragonwater.backend.Web.Support.Notice.dto.NoticeReqDto;
import com.dragonwater.backend.Web.Support.Notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {
    // 인터페이스 구현 완
    private final NoticeService noticeService;

    @GetMapping
    public ResponseEntity<?> getNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<?> addView(@PathVariable Long id) {
        log.info("id : {}", id);
        noticeService.addView(id);
        return ResponseEntity.ok("good");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showDetail(@PathVariable Long id) {
        NoticeReqDto detailNotice = noticeService.getDetailNotice(id);
        return ResponseEntity.ok(detailNotice);
    }
}