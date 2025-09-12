package com.dragonwater.backend.Web.Support.Claim.dto;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Support.Claim.domain.ClaimStatus;
import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Data
@Builder
@Slf4j
public class ClaimResDto {
    private Long claimId;
    private String customerName;
    private String type;
    private ClaimStatus status;
    private LocalDateTime createdAt;
    private ClaimOrderInform orderInform;
    private ClaimDetailContent detailContent;

    public static ClaimResDto of(Claims claims) {
        Orders order = claims.getOrder();
        Members member = order.getMember();
        String name = member.getName();
        if (member instanceof BranchMembers) {
            name += "-"+((BranchMembers) member).getBranchName();
        }
        return ClaimResDto.builder()
                .claimId(claims.getId())
                .status(claims.getClaimStatus())
                .customerName(name)
                .type(claims.getClaimType().getDescription())
                .createdAt(claims.getCreatedAt())
                .orderInform(ClaimOrderInform.of(order))
                .detailContent(ClaimDetailContent.of(claims))
                .build();
    }
}
