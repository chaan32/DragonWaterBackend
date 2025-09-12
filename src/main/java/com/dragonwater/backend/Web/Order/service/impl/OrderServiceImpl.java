package com.dragonwater.backend.Web.Order.service.impl;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.OrderNotFoundException;
import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.dto.*;
import com.dragonwater.backend.Web.Order.repository.OrderRepository;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.dragonwater.backend.Web.Order.service.interf.StockService;
import com.dragonwater.backend.Web.Payment.domain.BatchPayment;
import com.dragonwater.backend.Web.Payment.domain.PaymentMethod;
import com.dragonwater.backend.Web.Payment.domain.PaymentStatus;
import com.dragonwater.backend.Web.Payment.dto.HQPaymentDto;
import com.dragonwater.backend.Web.Payment.dto.HeadquartersPaymentReqDto;
import com.dragonwater.backend.Web.Payment.repository.BatchPaymentRepository;
import com.dragonwater.backend.Web.Shipment.domain.Shipments;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    // 인터페이스 구현 완
    private final MemberService memberService;
    private final ProductService productService;

    // 인터페이스 구현 미완

    private final OrderRepository orderRepository;
    private final BatchPaymentRepository batchPaymentRepository;


    private final StockService stockService;

    @Transactional(readOnly = true)
    public Orders getOrdersById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

    }@Transactional(readOnly = true)
    public Orders getOrdersByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Optional<Orders> findOptionalOrders(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Transactional
    public Orders createOrder(OrderReqDto orderDto) {
        // 1. 주문 기본 정보 준비
        Shipments shipments = Shipments.of(orderDto.getShipmentInform());
        Members member = memberService.getMemberById(orderDto.getUserId());
        String newOrderNumber = generateOrderNumber();
        Orders orders = Orders.of(orderDto, member, newOrderNumber);
        stockService.decreaseStockForOrder(orderDto.getItems());

        List<OrderItems> orderItems = new LinkedList<>();
        for (OrderItemDto item : orderDto.getItems()) {
            Products product = productService.getProductById(item.getProductId());
            orderItems.add(OrderItems.of(item, orders, product));
        }

        orders.setItems(orderItems);
        orders.setShipment(shipments);
        if (orders.getTotalPrice().intValue() == 0) {
            orders.setPaymentStatus(PaymentStatus.APPROVED);
        }

        return orderRepository.save(orders);
    }




    // 결제하기 (본사)
    @Transactional
    public void orderHeadQuarters(String orderNumber) {
        Orders orders = getOrdersByOrderNumber(orderNumber);
        orders.pay();
    }

    // 주문 내역 가져오기
    @Transactional(readOnly = true)
    public List<OrderResDto> getAllOrders() {
        List<OrderResDto> dtos = new LinkedList<>();
        for (Orders orders : orderRepository.findAll()) {
            dtos.add(OrderResDto.of(orders));
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public List<OrderResDto> getOrdersPage() {
        List<OrderResDto> dtos = new LinkedList<>();
        for (Orders orders : orderRepository.findAll()) {
            dtos.add(OrderResDto.of(orders));
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public List<OrderResDtoV2> getAllOrdersV2() {
        List<OrderResDtoV2> dtos = new LinkedList<>();
        for (Orders orders : orderRepository.findAll()) {
            dtos.add(OrderResDtoV2.of(orders));
        }

        return dtos;
    }

    @Transactional
    public void enrollTrackingNumber(String trackingNumber, Long orderId) {
        Orders ordersById = this.getOrdersById(orderId);
        Shipments shipment = ordersById.getShipment();
        shipment.processShipment(trackingNumber);
    }

    @Transactional(readOnly = true)
    public List<OrdersClaimResDto> getOrderList(Long userId) {
        List<Orders> orders = orderRepository.findByMemberId(userId);
        List<OrdersClaimResDto> dtos = new LinkedList<>();
        for (Orders order : orders) {
            dtos.add(OrdersClaimResDto.of(order));
        }
        return dtos;
    }


    @Transactional(readOnly = true)
    public List<Orders> findOrderByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }

    @Transactional
    public void payByOrderNumber(String orderNumber) {
        Orders orders = orderRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new OrderNotFoundException());
        orders.pay();
    }

    @Transactional
    public HQPaymentDto paymentHeadquarters(HeadquartersPaymentReqDto dto, Long memberId) {
        // 여기서 BatchPayment 저장 해야 함
        /**
         *              필요한 거
         *              1. 본사 객체 -> 멤버아이리도
         *              2. 생성된 orderNumber -> generateBatchNumber()
         *              3. 주문 리스트 -> 리스트에 넣으면 됨
         *              4. 총 합 금액 -> totalP
         *              5. paymentMethod -> dto.getPayMethod()
         */
        String orderNumber = generateBatchOrderNumber();
        BigDecimal totalP = BigDecimal.ZERO;
        HeadQuarterMembers member = (HeadQuarterMembers) memberService.getMemberById(memberId);
        List<Orders> orders = new LinkedList<>();

        for (Long id : dto.getOrderId()) {
            Orders order = this.getOrdersById(id);
            log.info("order Number - branches :{} / number: {}",order.getMember().getName(), order.getOrderNumber());
            orders.add(order);
            totalP = totalP.add(order.getTotalPrice());
        }
        BatchPayment batchPayment = BatchPayment.of(
                member, orders, orderNumber, totalP,
                dto.getPaymethod().equals("card")? PaymentMethod.CARD:PaymentMethod.BANK_TRANSFER);
        for (Orders order : orders) {
          order.setBatchPayment(batchPayment);
        }
        batchPaymentRepository.save(batchPayment);
        return HQPaymentDto.of(orderNumber, totalP.intValue());
    }

    @Transactional
    public void payHeadquartersOrder(String orderNumber) {
        BatchPayment batchPayment = batchPaymentRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new OrderNotFoundException());
        batchPayment.pay();
        batchPayment.getBatchOrders();
        for (Orders order : batchPayment.getBatchOrders()) {
            order.pay();
        }
    }

    @Transactional(readOnly = true)
    public BatchPayment getBatchPaymentByOrderNumber(String orderNumber) {
        return batchPaymentRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new OrderNotFoundException());
    }


    private String generateOrderNumber() {
        final String ORDER_PREFIX = "ORD-TEST";
        LocalDate currentDate = LocalDate.now();
        String uuid = String.valueOf(UUID.randomUUID());
        return ORDER_PREFIX
                + currentDate.getYear()+currentDate.getMonthValue()+currentDate.getDayOfMonth()
                + uuid.substring(10);
    }

    private String generateBatchOrderNumber() {
        final String BATCH_ORDER_PREFIX = "BATCH-TEST";
        LocalDate currentDate = LocalDate.now();
        String uuid = String.valueOf(UUID.randomUUID());
        return BATCH_ORDER_PREFIX
                + currentDate.getYear()+currentDate.getMonthValue()+currentDate.getDayOfMonth()
                + uuid.substring(10);
    }
}
