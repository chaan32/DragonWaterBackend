package com.dragonwater.backend.Web.Support.Notice.dto;

import com.dragonwater.backend.Web.Support.Notice.domain.Notices;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NoticeReqDto {
    private Long id;
    private String title;
    private String content;
    private Boolean pinned;
    private LocalDateTime createdAt;
    private Long views;

    public static NoticeReqDto of(Notices notice) {
        return NoticeReqDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .pinned(notice.getPinned())
                .views(notice.getViews())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}
