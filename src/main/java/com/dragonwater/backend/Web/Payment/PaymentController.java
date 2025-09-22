package com.dragonwater.backend.Web.Payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

// 임시 DTO
record PaymentValidationRequest(String productId, Integer amount) {}
record PaymentValidationResponse(String merchant_uid, Integer amount, String productName) {}
record WebhookPayload(String imp_uid, String merchant_uid, String status) {}

@RestController
public class PaymentController {

    /**
     * 디버깅용: 서버가 살아있는지 확인하는 주소
     * http://localhost:1111/ 로 접속해보세요.
     */
    @GetMapping("/")
    public String healthCheck() {
        return "서버 정상 동작 중! 이제 /payment.html 또는 /test.html 에 접속해보세요.";
    }

    @PostMapping("/api/payment/validation")
    public ResponseEntity<PaymentValidationResponse> validatePayment(@RequestBody PaymentValidationRequest request) {
        final int FIXED_AMOUNT = 100;
        if (!request.amount().equals(FIXED_AMOUNT)) {
            return ResponseEntity.badRequest().build();
        }
        String merchantUid = UUID.randomUUID().toString();
        System.out.printf("주문 생성: 주문번호=%s, 금액=%d%n", merchantUid, FIXED_AMOUNT);
        PaymentValidationResponse response = new PaymentValidationResponse(merchantUid, FIXED_AMOUNT, "테스트 상품");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/payment/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookPayload payload) {
        System.out.println("웹훅 수신: " + payload);
        return ResponseEntity.ok("success");
    }
}