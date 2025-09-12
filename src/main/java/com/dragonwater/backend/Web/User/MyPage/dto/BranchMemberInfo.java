package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class BranchMemberInfo {
    private String branchName;
    private BigDecimal totalPaymentPrice;

    public static BranchMemberInfo of(BranchMembers members) {
        BigDecimal total = BigDecimal.ZERO;

        for (Orders order : members.getOrders()) {
            total = total.add(order.getTotalPrice());
        }


        return BranchMemberInfo.builder()
                .branchName(members.getBranchName())
                .totalPaymentPrice(total)
                .build();
    }
}
