package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Data
@Builder
public class MPHeadquartersDashboardResDto {
    private Long id;
    private Integer branchNumber; // 활성 지점 갯수
    private Integer totalOrders; // 전체 주문 횟수
    private BigDecimal totalAmount; // 총 주문 금액
    private List<MPBranchesDto> branchesData;

    public static MPHeadquartersDashboardResDto of(HeadQuarterMembers member) {
        List<BranchMembers> branches = member.getBranches();
        List<MPBranchesDto> branchesDataList = new LinkedList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalOrderCount = 0;

        for (BranchMembers branch : branches) {
            List<Orders> branchOrders = branch.getOrders();
            totalOrderCount += branchOrders.size(); // 지점별 주문 횟수 누적

            for (Orders order : branchOrders) {
                // 2. add() 메소드 사용 & Orders 루프에서 한 번만 더하기
                if (order.getTotalPrice() != null) {
                    totalAmount = totalAmount.add(order.getTotalPrice());
                }
                branchesDataList.add(MPBranchesDto.of(order));
            }
        }
        log.info("TOTAL : {}", totalAmount);
        return MPHeadquartersDashboardResDto.builder()
                .id(member.getId())
                .branchNumber(branches.size())
                .totalOrders(totalOrderCount) // 계산된 전체 주문 횟수
                .totalAmount(totalAmount)     // 계산된 총 주문 금액
                .branchesData(branchesDataList)
                .build();
    }
}
