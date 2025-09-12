package com.dragonwater.backend.Web.Support.Notice.dto;

import lombok.Data;

@Data
public class AdminNoticeAddReqDto {
    private String title;
    private String content;
    private Boolean pinned;
}
