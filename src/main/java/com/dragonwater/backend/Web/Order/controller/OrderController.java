package com.dragonwater.backend.Web.Order.controller;

import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Web.KakaoNotify.service.KakaoNotiService;
import com.dragonwater.backend.Web.Notify.service.NotifyService;
import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.dto.OrderReqDto;
import com.dragonwater.backend.Web.Order.dto.OrdersClaimResDto;
import com.dragonwater.backend.Web.Order.dto.PreOrderResponseDto;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import com.dragonwater.backend.Web.Shipment.dto.SameWithRecipientReqDto;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.IndividualMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final NotifyService notifyService;

    private final KakaoNotiService kakaoNotiService;



    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/recipient/same/{userId}")
    public ResponseEntity<?> getRecipientInform(@PathVariable Long userId) {
        Members member = memberService.getMemberById(userId);
        return ResponseEntity.ok(SameWithRecipientReqDto.of(member));
    }

    @PostMapping
    public ResponseEntity<?> order(@RequestBody OrderReqDto orderDto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Orders order = orderService.createOrder(orderDto);


        Members member = order.getMember();
        if (member instanceof BranchMembers) {

            HashMap<String, String> vars = orderService.makeVars(order);
            kakaoNotiService.requestSend(vars, false);
            kakaoNotiService.requestSend(vars, true);
        }
        else if (member instanceof IndividualMembers && order.getTotalPrice().equals(BigDecimal.ZERO)){
            HashMap<String, String> vars = orderService.makeVars(order);
            kakaoNotiService.requestSend(vars, false);
            kakaoNotiService.requestSend(vars, true);

            orderService.subPoints(order);
            orderService.addPoints(order);

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
    @GetMapping("/point")
    public BigDecimal getMyPoint(HttpServletRequest request){

        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));

        Members member = memberService.getMemberById(memberId);

        if (member instanceof IndividualMembers){
            IndividualMembers member1 = (IndividualMembers) member;
            return member1.getMemberShipPoints();
        }
        return BigDecimal.ZERO;
    }

    private String getProductName(Orders orders) {
        List<OrderItems> items = orders.getItems();
        String productName = items.get(0).getProduct().getName();
        if (items.size() > 1) {
            productName += "외 " + (items.size() - 1) + "종";
        }
        return productName;
    }

    private HashMap<String, String> makeVars(Orders orders) {
        HashMap<String, String> vars = new HashMap<>();
        BranchMembers member = (BranchMembers) orders.getMember();
        vars.put("phone",member.getManagerPhone());
        vars.put("name",member.getName()+"-"+member.getBranchName());
        vars.put("orderNumber",orders.getOrderNumber());
        vars.put("productsName", makeProductsName(orders));
        vars.put("orderDate", makeOrderDate(orders.getCreatedAt()));
        vars.put("deliveryAddress",makeDeliveryAddress(orders));
        vars.put("orderPrice", makeOrderPrice(orders));
        return vars;
    }

    private String makeProductsName(Orders orders) {
        List<OrderItems> items = orders.getItems();
        int size = items.size();
        String productName = orders.getItems().get(0).getProduct().getName();
        if (size > 1){
            productName += "외 "+(size-1)+"종";
        }
        return productName;
    }
    private String makeOrderDate(LocalDateTime orderDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");
        return orderDate.format(formatter);
    }

    private String makeDeliveryAddress(Orders orders) {
        Shipments shipment = orders.getShipment();
        return shipment.getAddress() + " " + shipment.getDetailAddress();
    }

    private String makeOrderPrice(Orders orders) {
        DecimalFormat formatter = new DecimalFormat("###,###");
        BigDecimal totalPrice = orders.getTotalPrice();
        return formatter.format(totalPrice);
    }
}
