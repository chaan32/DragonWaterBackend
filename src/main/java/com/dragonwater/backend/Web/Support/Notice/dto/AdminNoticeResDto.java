package com.dragonwater.backend.Web.Support.Notice.dto;

import com.dragonwater.backend.Web.Support.Notice.domain.Notices;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminNoticeResDto {
    private Long id;
    private String title;
    private String content;
    private Boolean pinned;
    private LocalDateTime createdAt;

    public static AdminNoticeResDto of(Notices notice) {
        return AdminNoticeResDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .pinned(notice.getPinned())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
