package com.dragonwater.backend.Web.Statistics.service;

import com.dragonwater.backend.Web.Order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService{

    private final OrderRepository orderRepository;

    @Override
    public BigDecimal totalCumulativeSales() {
        return orderRepository.getAllSales();
    }


    @Override
    public List<Object[]> yearlyCumulativeSales() {
        return orderRepository.findTotalSalesByYear();
    }

    @Override
    public List<Object[]> monthlyCumulativeSales(int year) {
        return orderRepository.findTotalSalesByMonth(year);
    }


    @Override
    public List<Object[]> dailyCumulativeSales(int year, int month) {
        return orderRepository.findTotalSalesByDay(year, month);
    }

    @Override
    public int totalCumulativeOrders() {
        return orderRepository.getAllOrdersCount();
    }

    @Override
    public List<Object[]> yearlyCumulativeOrders() {

        return orderRepository.findTotalOrdersByYear();
    }

    @Override
    public List<Object[]> monthlyCumulativeOrders(int year) {
        return orderRepository.findTotalOrdersByMonth(year);
    }

    @Override
    public List<Object[]> dailyCumulativeOrders(int year, int month) {
        return orderRepository.findTotalOrdersByDay (year, month);
    }

    public BigDecimal getCumulativeSales() {
        return orderRepository.getAllSales();
    }

    public int getCumulativeOrders() {
        return orderRepository.getAllOrdersCount();
    }
}
