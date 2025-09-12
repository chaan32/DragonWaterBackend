package com.dragonwater.backend.Web.Order.repository;

import com.dragonwater.backend.Web.Order.domain.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByMemberId(Long id);
    Optional<Orders> findByOrderNumber (String orderNumber);

    Page<Orders> findByMemberId(Long memberId, Pageable pageable);

    // 연도 별 데이터 추출하기
    @Query("SELECT FUNCTION('YEAR', o.createdAt) AS year, SUM(o.totalPrice) FROM Orders o GROUP BY FUNCTION('YEAR', o.createdAt)")
    List<Object[]> findTotalSalesByYear();

    // 월 별 데이터 추출하기
    @Query("SELECT FUNCTION('MONTH', o.createdAt) AS month, SUM(o.totalPrice) FROM Orders o WHERE FUNCTION('YEAR', o.createdAt) = :year GROUP BY FUNCTION('MONTH', o.createdAt)")
    List<Object[]> findTotalSalesByMonth(@Param("year") int year);

    // 일 별 데이터 추출하기
    @Query("SELECT FUNCTION('DAY', o.createdAt) AS day, SUM(o.totalPrice) FROM Orders o WHERE FUNCTION('YEAR', o.createdAt) = :year AND FUNCTION('MONTH', o.createdAt) = :month GROUP BY FUNCTION('DAY', o.createdAt)")
    List<Object[]> findTotalSalesByDay(@Param("year") int year, @Param("month") int month);

    // 특정 연도 데이터 추출하기
    @Query("SELECT o FROM Orders o WHERE o.createdAt BETWEEN :startOfYear AND :endOfYear")
    List<Orders> findSalesByYear(LocalDateTime startOfYear, LocalDateTime endOfYear);

    // 연도 별 주문 수 추출하기
    @Query("SELECT FUNCTION('YEAR', o.createdAt) AS year, COUNT(o.totalPrice) FROM Orders o GROUP BY FUNCTION('YEAR', o.createdAt)")
    List<Object[]> findTotalOrdersByYear();

    // 월 별 주문 수 추출하기
    @Query("SELECT FUNCTION('MONTH', o.createdAt) AS month, COUNT(o) FROM Orders o WHERE FUNCTION('YEAR', o.createdAt) = :year GROUP BY FUNCTION('MONTH', o.createdAt)")
    List<Object[]> findTotalOrdersByMonth(@Param("year") int year);

    // 일 별 주문 수 추출하기
    @Query("SELECT FUNCTION('DAY', o.createdAt) AS day, COUNT(o) FROM Orders o WHERE FUNCTION('YEAR', o.createdAt) = :year AND FUNCTION('MONTH', o.createdAt) = :month GROUP BY FUNCTION('DAY', o.createdAt)")
    List<Object[]> findTotalOrdersByDay(@Param("year") int year, @Param("month") int month);

    // 지금까지 누적 주문 액 추출하기
    @Query("SELECT SUM (o.totalPrice) FROM Orders o")
    BigDecimal getAllSales();

    // 지금까지 누적 주문 건수 추출하기
    @Query("SELECT COUNT (o) FROM Orders o")
    int getAllOrdersCount();
}
