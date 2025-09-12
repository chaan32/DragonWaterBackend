package com.dragonwater.backend.Web.Statistics.service;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {
    // 누적 매출
    BigDecimal totalCumulativeSales();

    // 연도별 매출
    List<Object[]> yearlyCumulativeSales();

    // 월별 매출
    List<Object[]> monthlyCumulativeSales(int year);

    // 날짜 별 매출
    List<Object[]> dailyCumulativeSales(int year, int month);

    // 누적 주문 수
    int totalCumulativeOrders();

    // 연도별 주문 수
    List<Object[]> yearlyCumulativeOrders();

    // 월별 주문 수
    List<Object[]> monthlyCumulativeOrders(int year);

    // 날짜 별 주문 수
    List<Object[]> dailyCumulativeOrders(int year, int month);

    BigDecimal getCumulativeSales();
    int getCumulativeOrders();
}
