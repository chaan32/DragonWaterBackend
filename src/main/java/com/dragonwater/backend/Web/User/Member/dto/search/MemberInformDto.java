package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.dto.OrderMembersResDto;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.domain.SpecializeProducts;
import com.dragonwater.backend.Web.User.Member.domain.*;
import com.dragonwater.backend.Web.User.Member.dto.product.ProductMinimalInformResDto;
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
    private List<ProductMinimalInformResDto> specializeProduct;



    private HeadQuartersResDto headQuartersInform;
    private BranchResDto branchResDto;
    private IndividualsResDto individualInform;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderMembersResDto> orders;
    private BigDecimal totalAmount;

    public static MemberInformDto of(Members member) {
        List<ProductMinimalInformResDto> specializeProduct = new LinkedList<>();
        for (SpecializeProducts products : member.getSpecializedProducts()) {
            specializeProduct.add(ProductMinimalInformResDto.of(products.getProduct()));
        }


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
                    .specializeProduct(specializeProduct)
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
                    .specializeProduct(specializeProduct)
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
                    .specializeProduct(specializeProduct)
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .totalAmount(ta)
                    .orders(ords)
                    .build();
        }
    }
}
