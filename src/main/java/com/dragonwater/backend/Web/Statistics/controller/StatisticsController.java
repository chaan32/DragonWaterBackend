package com.dragonwater.backend.Web.Statistics.controller;

import com.dragonwater.backend.Web.Statistics.service.StatisticsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequestMapping("/api/admin/statistics")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/sales/total")
    public ResponseEntity<?> getTotalSales() {
        return ResponseEntity.ok(statisticsService.totalCumulativeSales());
    }

    @GetMapping("/sales/yearly")
    public ResponseEntity<?> getYearlySales() {
        return ResponseEntity.ok(statisticsService.yearlyCumulativeSales());
    }

    // 월별
    @GetMapping("/sales/monthly/{year}")
    public ResponseEntity<?> getMonthlySales(@PathVariable int year) {
        return ResponseEntity.ok(statisticsService.monthlyCumulativeSales(year));
    }

    // 일별
    @GetMapping("/sales/daily/{year}/{month}")
    public ResponseEntity<?> getDailySales(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(statisticsService.dailyCumulativeSales(year, month));
    }

    @GetMapping("/orders/total")
    public ResponseEntity<?> getTotalOrders() {
        return ResponseEntity.ok(statisticsService.totalCumulativeOrders());
    }

    @GetMapping("/orders/yearly")
    public ResponseEntity<?> getYearlyOrders() {
        return ResponseEntity.ok(statisticsService.yearlyCumulativeOrders());
    }

    @GetMapping("/orders/monthly/{year}")
    public ResponseEntity<?> getMonthlyOrders(@PathVariable int year) {
        return ResponseEntity.ok(statisticsService.monthlyCumulativeOrders(year));
    }

    @GetMapping("/orders/daily/{year}/{month}")
    public ResponseEntity<?> getDailyOrders(@PathVariable int year, @PathVariable int month) {
        return ResponseEntity.ok(statisticsService.dailyCumulativeOrders(year, month));
    }

    @GetMapping("/cumulative")
    public ResponseEntity<?> getCumulativeRecord() {

        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("sales", statisticsService.getCumulativeSales());
        response.put("orders", statisticsService.getCumulativeOrders());
        
        return ResponseEntity.ok(response);
    }

}
