package com.dragonwater.backend.Web.User.Member.controller.Admin;

import com.dragonwater.backend.Web.Order.dto.OrderResDto;
import com.dragonwater.backend.Web.Order.dto.OrderResDtoV2;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping("/v2")
    public ResponseEntity<?> getAllOrders() {
        List<OrderResDto> allOrders = orderService.getAllOrders();
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", allOrders);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<?> getAllOrdersV2() {
        List<OrderResDtoV2> allOrders = orderService.getAllOrdersV2();
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", allOrders);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/tracking")
    public ResponseEntity<?> uploadTrackingNumber(@PathVariable Long orderId, @RequestBody Map<String, String> body) { // ✨ Map으로 받기

        String trackingNumber = body.get("trackingNumber");

        orderService.enrollTrackingNumber(trackingNumber, orderId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
}
