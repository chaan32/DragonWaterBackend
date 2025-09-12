package com.dragonwater.backend.Web.Certification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NaverSmsResDto {
    String requestId;
    LocalDateTime requestTime;
    String statusCode;
    String statusName;
}
