package com.dragonwater.backend.Web.Order.dto;

import com.dragonwater.backend.Web.Shipment.dto.ShipmentReqDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class OrderReqDto {
    public OrderReqDto(Long userId, List<OrderItemDto> items, ShipmentReqDto shipmentInform, BigDecimal totalPrice, BigDecimal productPrice, BigDecimal couponDiscountPrice, BigDecimal pointDiscountPrice, BigDecimal shipmentFee, String paymentMethod) {
        this.userId = userId;
        this.items = items;
        this.shipmentInform = shipmentInform;
        this.totalPrice = totalPrice;
        this.productPrice = productPrice;
        this.couponDiscountPrice = couponDiscountPrice;
        this.pointDiscountPrice = pointDiscountPrice;
        this.shipmentFee = shipmentFee;
        this.paymentMethod = paymentMethod;
    }

    private Long userId;

    // 아이템을 뭐를 몇개를 주문 했는지?
    private List<OrderItemDto> items;

    // 배송 정보
    private ShipmentReqDto shipmentInform;

    // 금액 정보
    private BigDecimal totalPrice;
    private BigDecimal productPrice;
    private BigDecimal couponDiscountPrice;
    private BigDecimal pointDiscountPrice;
    private BigDecimal shipmentFee;

    // 결제 정보
    private String paymentMethod;
}
