package com.dragonwater.backend.Web.Order.service.interf;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.dto.*;
import com.dragonwater.backend.Web.Payment.domain.BatchPayment;
import com.dragonwater.backend.Web.Payment.dto.HQPaymentDto;
import com.dragonwater.backend.Web.Payment.dto.HeadquartersPaymentReqDto;
import org.hibernate.query.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    /**
     * 주문을 가져오는 메소드
     * @param orderId Orders를 식별할 수 있는 id 값
     * @return
     */
    Orders getOrdersById(Long orderId);

    /**
     * 주문 번호를 통해서 주문을 가져오는 메소드
     * @param orderNumber Orders를 식별할 수 있는 주문 번호 값
     * @return
     */
    Orders getOrdersByOrderNumber(String orderNumber);

    /**
     * 옴셔널 주문을 가져오는 메소드
     * @param orderId
     * @return
     */
    Optional<Orders> findOptionalOrders(Long orderId);

    /**
     * 주문을 생성하는 메소드
     * @param orderDto 주문 내용을 담은 DTO
     * @return
     */
    Orders createOrder(OrderReqDto orderDto);


    /**
     * 본사가 결제를 하는 메소드
     * @param orderNumber 주문 번호를 식별할 수 있는 주문 번호
     */
    void orderHeadQuarters(String orderNumber);

    /**
     * 주문 내역 가져오는 메소드 V1
     * @return
     */
    List<OrderResDto> getAllOrders();

    /**
     * 주문 내역 가져오는 메소드 V2
     * @return
     */
    public List<OrderResDtoV2> getAllOrdersV2();

    /**
     * 주문 페이지에서 주문 내역을 가져오는 메소드
     * @return
     */
    List<OrderResDto> getOrdersPage();


    /**
     * 송장 번호를 추가하는 메소드
     * @param trackingNumber
     * @param orderId
     */
    void enrollTrackingNumber(String trackingNumber, Long orderId);

    /**
     * 멤버의 주문 리스트를 가져오는 메소드
     * @param userId 멤버를 식별할 수 있는 멤버 id
     * @return
     */
    List<OrdersClaimResDto> getOrderList(Long userId);


    /**
     * 멤버아이디로 주문 객체 가져오는 메소드
     * @param memberId 멤버를 식별할 수 있는 멤버 id
     * @return
     */
    List<Orders> findOrderByMemberId(Long memberId);

    /**
     * 주문 번호를 통해서 주문 처리하는 메소드
     * @param orderNumber 주문을 식별할 수 있는 주문 id
     */
    void payByOrderNumber(String orderNumber);

    /**
     * 본사가 주문을 처리할 수 있도록 준비하는 메소드
     * @param dto 주문할 정보들을 포함하는 dto
     * @param memberId  본사를 식별할 수 있는 id 값
     * @return
     */
    HQPaymentDto paymentHeadquarters(HeadquartersPaymentReqDto dto, Long memberId);

    /**
     * 주문 번호를 통해서 주문을 결제 처리를 하는 메소드
     * @param orderNumber 주문을 식별할 수 있는 주문 번호
     */
    void payHeadquartersOrder(String orderNumber);

    /**
     * 주문 번호를 통해서 가져오는 메소드
     * @param orderNumber
     * @return
     */
    BatchPayment getBatchPaymentByOrderNumber(String orderNumber);

    /**
     * 오더 객체를 가지고 카카오톡 알림톡에 필요한 변수를 생성하는 메소드
     * @param orders 오더 객체
     * @return
     */
    HashMap<String, String> makeVars(Orders orders);
}
