package com.dragonwater.backend.Web.Support.Claim.dto;

import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import lombok.Builder;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class ClaimDetailContent {
    private String title;
    private String content;
    private List<String> filesUrl;

    public static ClaimDetailContent of(Claims claims) {


        List<String> filesUrl = new LinkedList<>();
        if (claims.getImage1Url() != null) {
            filesUrl.add(claims.getImage1Url());
        }
        if (claims.getImage2Url() != null) {
            filesUrl.add(claims.getImage2Url());
        }
        if (claims.getImage3Url() != null) {
            filesUrl.add(claims.getImage3Url());
        }
        if (claims.getImage4Url() != null) {
            filesUrl.add(claims.getImage4Url());
        }
        if (claims.getImage5Url() != null) {
            filesUrl.add(claims.getImage5Url());
        }
        return ClaimDetailContent.builder()
                .title(claims.getTitle())
                .content(claims.getDescription())
                .filesUrl(filesUrl)
                .build();
    }
}
