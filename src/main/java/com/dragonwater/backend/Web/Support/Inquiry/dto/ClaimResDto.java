package com.dragonwater.backend.Web.Support.Inquiry.dto;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Support.Claim.domain.ClaimStatus;
import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
public class ClaimResDto {
    private Long claimId;
    private String title;
    private String content;
    private ClaimStatus status;
    private String answer;
    private String orderNumber;
    private LocalDateTime createdAt;
    private List<OrderItemInqResDto> items;

    public static ClaimResDto of(Claims claim) {
        Orders order = claim.getOrder();
        List<OrderItemInqResDto> items= new LinkedList<>();
        if (order != null) {
            List<OrderItems> items1 = order.getItems();
            for (OrderItems orderItems : items1) {
                items.add(OrderItemInqResDto.of(orderItems));
            }
        }
        return ClaimResDto.builder()
                .claimId(claim.getId())
                .title(claim.getTitle())
                .content(claim.getDescription())
                .status(claim.getClaimStatus())
                .answer(claim.getRejectedReason())
                .orderNumber(order == null ? null : order.getOrderNumber())
                .items(items)
                .build();
    }
}
