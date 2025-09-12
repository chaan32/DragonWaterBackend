package com.dragonwater.backend.Web.User.Member.controller.Admin;

import com.dragonwater.backend.Web.Support.Notice.domain.Notices;
import com.dragonwater.backend.Web.Support.Notice.dto.AdminNoticeAddReqDto;
import com.dragonwater.backend.Web.Support.Notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminNoticeController {
    // 인터페이스 구현 완
    private final NoticeService noticeService;

    // 공지사항 가져오기
    @GetMapping("/notices")
    public ResponseEntity<?> getNotices() {
        return ResponseEntity.ok(noticeService.getAdminAllNotices());
    }
    // 공지사항 작성하기
    @PostMapping("/notices")
    public ResponseEntity<?> uploadNotices(@RequestBody AdminNoticeAddReqDto dto) {
        log.info("dot : {}", dto.toString());
        Notices notice = noticeService.addNotices(dto);
        return ResponseEntity.ok(notice);
    }
    // 공지사항 삭제하기
    @DeleteMapping("/notices/{id}")
    public ResponseEntity<?> deleteNotices(@PathVariable Long id) {
        boolean check = noticeService.deleteNotice(id);
        if (check) {
            return ResponseEntity.ok("제대로 삭제 했어요.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 이유로 공지사항 삭제를 실패했어요.");
    }

    // 공지사항 수정하기
    @PutMapping("/notices/{id}")
    public ResponseEntity<?> editNotice(@PathVariable Long id, @RequestBody AdminNoticeAddReqDto dto) {
        log.info("dot : {}", dto.toString());
        Notices notice = noticeService.editNotices(dto, id);
        return ResponseEntity.ok(notice);
    }
}
