package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.dto.OrderMembersResDto;
import com.dragonwater.backend.Web.User.Member.domain.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
public class MemberInformDto {
    private Long id;
    private String loginId;
    private String address;
    private String detailAddress;
    private String postalCode;
    private String phone;
    private String email;


    private HeadQuartersResDto headQuartersInform;
    private BranchResDto branchResDto;
    private IndividualsResDto individualInform;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderMembersResDto> orders;
    private BigDecimal totalAmount;

    public static MemberInformDto of(Members member) {
        if (member instanceof AdminMembers) {
            return null;
        }


        List<OrderMembersResDto> ords = new LinkedList<>();
        BigDecimal ta = BigDecimal.ZERO;
        for (Orders order : member.getOrders()) {
            OrderMembersResDto orderMembersResDto = OrderMembersResDto.of(order);
            ords.add(orderMembersResDto);
            ta = ta.add(orderMembersResDto.getProductPrice());
        }

        if (member instanceof IndividualMembers) {
            IndividualsResDto individualsResDto = IndividualsResDto.of((IndividualMembers) member);
            return MemberInformDto.builder()
                    .id(member.getId())
                    .loginId(member.getLoginId())
                    .address(member.getAddress())
                    .detailAddress(member.getDetailAddress())
                    .postalCode((member.getZipCode()))
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .individualInform(individualsResDto)
                    .totalAmount(ta)
                    .orders(ords)
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .build();
        } else if (member instanceof BranchMembers) {
            BranchResDto branchMembers = BranchResDto.of((BranchMembers) member);
            return MemberInformDto.builder()
                    .id(member.getId())
                    .loginId(member.getLoginId())
                    .address(member.getAddress())
                    .detailAddress(member.getDetailAddress())
                    .postalCode((member.getZipCode()))
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .branchResDto(branchMembers)
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .totalAmount(ta)
                    .orders(ords)
                    .build();
        } else {
            HeadQuartersResDto headQuartersResDto = HeadQuartersResDto.of((HeadQuarterMembers) member);
            return MemberInformDto.builder()
                    .id(member.getId())
                    .loginId(member.getLoginId())
                    .address(member.getAddress())
                    .detailAddress(member.getDetailAddress())
                    .postalCode((member.getZipCode()))
                    .email(member.getEmail())
                    .phone(member.getPhone())
                    .headQuartersInform(headQuartersResDto)
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .totalAmount(ta)
                    .orders(ords)
                    .build();
        }
    }
}
