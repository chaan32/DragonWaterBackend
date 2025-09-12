package com.dragonwater.backend.Web.Order.controller;

import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Web.Notify.service.NotifyService;
import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.dto.OrderReqDto;
import com.dragonwater.backend.Web.Order.dto.OrdersClaimResDto;
import com.dragonwater.backend.Web.Order.dto.PreOrderResponseDto;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.dragonwater.backend.Web.Shipment.dto.SameWithRecipientReqDto;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final NotifyService notifyService;



    private final JwtTokenProvider provider;

    @GetMapping("/recipient/same/{userId}")
    public ResponseEntity<?> getRecipientInform(@PathVariable Long userId) {
        Members member = memberService.getMemberById(userId);
        return ResponseEntity.ok(SameWithRecipientReqDto.of(member));
    }

    @PostMapping
    public ResponseEntity<?> order(@RequestBody OrderReqDto orderDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Orders order = orderService.createOrder(orderDto);
        String productName = getProductName(order);

        Members member = order.getMember();
        if (member instanceof BranchMembers) {
            notifyService.notifySuccessOrderToCustomerBySMS(order.getMember().getPhone(), productName);
            notifyService.notifyOrderToAdminBySMS(order.getMember().getRole());
        }
        return ResponseEntity.ok(PreOrderResponseDto.of(order));
    }

    @GetMapping("/in-claims/{userId}")
    public ResponseEntity<?> getOrdersInClaims(@PathVariable Long userId) {
        List<OrdersClaimResDto> orderList = orderService.getOrderList(userId);
        return ResponseEntity.ok(orderList);
    }

    @PostMapping("/{orderNumber}/pay")
    public ResponseEntity<?> payBranchesOrders(@PathVariable String orderNumber) {
        try {
            orderService.orderHeadQuarters(orderNumber);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 중 오류가 발생했습니다.");
        }
    }

    private String getProductName(Orders orders) {
        List<OrderItems> items = orders.getItems();
        String productName = items.get(0).getProduct().getName();
        if (items.size() > 1) {
            productName += "외 " + (items.size() - 1) + "종";
        }
        return productName;
    }
}
