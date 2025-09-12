package com.dragonwater.backend.Web.Payment.controller;

import com.dragonwater.backend.Config.Common.dto.ApiResponse;
import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Config.Redis.RedisDao;
import com.dragonwater.backend.Web.Notify.service.NotifyService;
import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.dragonwater.backend.Web.Payment.domain.BatchPayment;
import com.dragonwater.backend.Web.Payment.dto.HQPaymentDto;
import com.dragonwater.backend.Web.Payment.dto.HeadquartersPaymentReqDto;
import com.dragonwater.backend.Web.Payment.dto.IndividualPaymentReqDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Map;

record PaymentPreparationResponse(String mid, String orderId, int amount, String editDate, String signature) {}

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    // 인터페이스 구현 완
    private final OrderService orderService;
    private final NotifyService notifyService;
    private final RedisDao redisDao;

    // 인터페이스 구현 미완

    private final JwtTokenProvider tokenProvider;
    private final ServletRequest httpServletRequest;

    @Value("${nicepayments.mid}")
    private String mid;

    @Value("${nicepayments.merchant-key}")
    private String merchantKey;

    @Value("${nicepayments.client-key}")
    private String clientKey;

    @Value("${nicepayments.secret-key}")
    private String secretKey;

    private final String REDIS_PREFIX = "reqUrl";



    @PostMapping("/prepare")
    public ResponseEntity<ApiResponse<PaymentPreparationResponse>> preparePayment(@RequestBody IndividualPaymentReqDto dto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 주문 정보 조회
        Orders order = orderService.getOrdersByOrderNumber(dto.getOrderId());

        String requestUrl = this.getRequestUrl(request);


        // 결제 정보 요청 파라미터 생성
        String orderNumber = order.getOrderNumber();
        int totalPrice = order.getTotalPrice().intValue();

        String editDate = String.valueOf(Instant.now().toEpochMilli());

        String signature = generateSignature(editDate, String.valueOf(totalPrice));

        PaymentPreparationResponse response = new PaymentPreparationResponse(
                mid, orderNumber, totalPrice, editDate, signature
        );

        log.info("----/prepare 단계----");
        log.info("request : {}",requestUrl);

        log.info("Individual Payment preparation successful.");
        return ResponseEntity.ok(ApiResponse.success(response, "Payment preparation successful."));
    }
    @PostMapping("/prepare/headquarters")
    public ResponseEntity<ApiResponse<PaymentPreparationResponse>> prepareHeadquartersPayment(@RequestBody HeadquartersPaymentReqDto dto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String requestUrl = this.getRequestUrl(request);

        Long memberId = tokenProvider.getMemberId(request.getHeader("Authorization").substring(7));

        // 여기서 DB에 저장을 해야 돼
        HQPaymentDto paymentDto = orderService.paymentHeadquarters(dto, memberId);

        String editDate = String.valueOf(Instant.now().toEpochMilli());

        String signature = generateSignature(editDate, String.valueOf(paymentDto.getTotalPrice()));

        PaymentPreparationResponse response = new PaymentPreparationResponse(
                mid, paymentDto.getOrderNumber(), paymentDto.getTotalPrice(), editDate, signature
        );


        return ResponseEntity.ok(ApiResponse.success(response, "Payment preparation successful."));
    }

    private String getRequestUrl(HttpServletRequest httpServletRequest) {
        return ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build().toUriString();
    }




    // 나이스페이먼츠 서버로부터 결제 결과 통보 받는 API
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebHook(@RequestBody Map<String, Object> webhookPayload) {
        log.info("나이스페이먼츠로부터 결제 정보 받음");
        //이제 여기서 검증과 처리 로직을 추가하면 됨
        return ResponseEntity.ok().build();
    }


    @PostMapping(value = "/return", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void nicepayReturn(@RequestParam Map<String, String> form, HttpServletResponse response) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("----/return 단계----");
        log.info("🔥 NICEPAY return form: {}", form); // 여기서 실제 넘어오는 키 확인


        String authResultCode = form.get("authResultCode"); // "0000"이면 인증 성공
        String authResultMsg  = form.get("authResultMsg");
        String tid            = form.get("tid");
        String orderId        = form.get("orderId");
        String amountStr      = form.get("amount");


        if (!"0000".equals(authResultCode)) {
            // 실패 처리 후 프론트로 리다이렉트
            response.sendRedirect("https://dragonwater.co.kr/payment/result?status=fail&orderId=" +
                    URLEncoder.encode(orderId == null ? "" : orderId, StandardCharsets.UTF_8) +
                    "&msg=" + URLEncoder.encode(authResultMsg == null ? "" : authResultMsg, StandardCharsets.UTF_8));
            return;
        }

        int amount = Integer.parseInt(amountStr);

        HttpClient http = HttpClient.newHttpClient();
        String bodyJson = "{\"amount\":" + amount + "}";
        String beforeEncode = String.format("%s:%s", this.clientKey, this.secretKey);
        String key = Base64.getEncoder().encodeToString(beforeEncode.getBytes(StandardCharsets.UTF_8));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.nicepay.co.kr/v1/payments/" + tid))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + key)
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                .build();

        HttpResponse<String> res;
        try {
            res = http.send(req, HttpResponse.BodyHandlers.ofString());
            log.info("🔥 NICEPAY approve response: {}", res.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }

        // ✅ 응답 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(res.body());

        String resultCode = json.path("resultCode").asText();
        String resultMsg  = json.path("resultMsg").asText();
        String status     = json.path("status").asText();
        log.info("resultCode : {} resultMsg : {}, status : {}",resultCode,resultMsg,status);
        if ("0000".equals(resultCode) && "paid".equals(status)) {
            orderService.payByOrderNumber(orderId);
            response.sendRedirect("https://dragonwater.co.kr/payment/result?status=success&orderId=" +
                    URLEncoder.encode(orderId, StandardCharsets.UTF_8) +
                    "&tid=" + URLEncoder.encode(tid, StandardCharsets.UTF_8));
            // 여기서 주문 완료 문자 발송
            log.info("order Id : {}", orderId);
            Orders orders = orderService.getOrdersByOrderNumber(orderId);
            String productName = getProductName(orders);
            log.info("------ 지금 문자 보낸다 -------");
            notifyService.notifySuccessOrderToCustomerBySMS(orders.getMember().getPhone(), productName);
            notifyService.notifyOrderToAdminBySMS(orders.getMember().getRole());
            log.info("------ 지금 문자 보냈다 -------");
        } else {
            response.sendRedirect("https://dragonwater.co.kr/payment/result?status=fail&orderId=" +
                    URLEncoder.encode(orderId == null ? "" : orderId, StandardCharsets.UTF_8) +
                    "&msg=" + URLEncoder.encode(resultMsg, StandardCharsets.UTF_8));
        }
    }

    @PostMapping(value = "/return/headquarters", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void nicepayHQReturn(@RequestParam Map<String, String> form, HttpServletResponse response) throws IOException {
        log.info("🔥 NICEPAY - Headquarters Payments return form: {}", form);


        String authResultCode = form.get("authResultCode"); // "0000"이면 인증 성공
        String authResultMsg  = form.get("authResultMsg");
        String tid            = form.get("tid");
        String orderId        = form.get("orderId");
        String amountStr      = form.get("amount");



        if (!"0000".equals(authResultCode)) {
            // 실패 처리 후 프론트로 리다이렉트
            response.sendRedirect("https://dragonwater.co.kr/payment/result?status=fail&orderId=" +
                    URLEncoder.encode(orderId == null ? "" : orderId, StandardCharsets.UTF_8) +
                    "&msg=" + URLEncoder.encode(authResultMsg == null ? "" : authResultMsg, StandardCharsets.UTF_8));
            return;
        }

        int amount = Integer.parseInt(amountStr);

        HttpClient http = HttpClient.newHttpClient();
        String bodyJson = "{\"amount\":" + amount + "}";
        String beforeEncode = String.format("%s:%s", this.clientKey, this.secretKey);
        String key = Base64.getEncoder().encodeToString(beforeEncode.getBytes(StandardCharsets.UTF_8));

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.nicepay.co.kr/v1/payments/" + tid))
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + key)
                .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                .build();

        HttpResponse<String> res;
        try {
            res = http.send(req, HttpResponse.BodyHandlers.ofString());
            log.info("🔥 NICEPAY approve response: {}", res.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }

        // ✅ 응답 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(res.body());

        String resultCode = json.path("resultCode").asText();
        String resultMsg  = json.path("resultMsg").asText();
        String status     = json.path("status").asText();
        log.info("resultCode : {} resultMsg : {}, status : {}",resultCode,resultMsg,status);
        if ("0000".equals(resultCode) && "paid".equals(status)) {
            orderService.payHeadquartersOrder(orderId);
            response.sendRedirect("https://dragonwater.co.kr/payment/result?status=success&orderId=" +
                    URLEncoder.encode(orderId, StandardCharsets.UTF_8) +
                    "&tid=" + URLEncoder.encode(tid, StandardCharsets.UTF_8));
        } else {
            response.sendRedirect("https://dragonwater.co.kr/payment/result?status=fail&orderId=" +
                    URLEncoder.encode(orderId == null ? "" : orderId, StandardCharsets.UTF_8) +
                    "&msg=" + URLEncoder.encode(resultMsg, StandardCharsets.UTF_8));
        }
    }


    private String generateSignature(String editDate, String amount) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String message = mid + editDate + amount + merchantKey;

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(message.getBytes("UTF-8"));

        // Convert byte array to hex string
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    @PostMapping("/test/{orderNumber}")
    public String test(@PathVariable String orderNumber) {
        BatchPayment batchPaymentByOrderNumber = orderService.getBatchPaymentByOrderNumber(orderNumber);
        String re = batchPaymentByOrderNumber.getOrderNumber();
        for (Orders batchOrder : batchPaymentByOrderNumber.getBatchOrders()) {
            re+="/"+batchOrder.getOrderNumber();
        }
        return re;
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