package com.dragonwater.backend.Web.Order.service;

import com.dragonwater.backend.Web.Order.domain.BatchPaymentSequence;
import com.dragonwater.backend.Web.Order.domain.OrderSequence;
import com.dragonwater.backend.Web.Order.repository.BatchSequenceRepository;
import com.dragonwater.backend.Web.Order.repository.OrderSequenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderNumberGeneratorService {

    private final OrderSequenceRepository orderSequenceRepository;
    private static final String ORDER_PREFIX = "ORD";
    private static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final BatchSequenceRepository batchSequenceRepository;
    private static final String BATCH_PREFIX = "HBP";

    /**
     * 새로운 주문 번호를 생성합니다.
     * 형식: ORD-YYYY-MM-XXX (예: ORD-2025-08-001)
     *
     * @return 생성된 주문 번호
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(YEAR_MONTH_FORMATTER);

        // 🚨 중요: 락을 거는 메소드로 시퀀스를 조회합니다.
        // 이 시점에 '2025-08' 같은 특정 월의 시퀀스 데이터에 락이 걸립니다.
        OrderSequence sequence = orderSequenceRepository.findByIdWithPessimisticLock(yearMonth)
                .orElseGet(() -> {
                    log.info("새로운 월 시퀀스 생성: {}", yearMonth);
                    return new OrderSequence(yearMonth);
                });

        Long nextSequence = sequence.increaseAndGetSequence();
        orderSequenceRepository.save(sequence);

        String orderNumber = String.format("%s-%s-%03d",
                ORDER_PREFIX,
                yearMonth,
                nextSequence);

        log.info("생성된 주문 번호: {}", orderNumber);
        return orderNumber;
    }

    public String generateBatchNumber() {
        LocalDateTime now = LocalDateTime.now();
        String yearMonth = now.format(YEAR_MONTH_FORMATTER);

        // 🚨 중요: 여기도 락을 거는 메소드로 변경합니다.
        BatchPaymentSequence sequence = batchSequenceRepository.findByIdWithPessimisticLock(yearMonth)
                .orElseGet(() -> {
                    log.info("새로운 월 시퀀스 생성 : {}", yearMonth);
                    return new BatchPaymentSequence(yearMonth);
                });
        Long nextSequence = sequence.increaseAndGetSequence();

        batchSequenceRepository.save(sequence);

        String batchNumber = String.format("%s-%s-%03d",
                BATCH_PREFIX,
                yearMonth,
                nextSequence);
        log.info("생성된 Batch 결제 번호: {}", batchNumber);
        return batchNumber;
    }

    /**
     * 특정 연월의 현재 시퀀스를 조회합니다.
     *
     * @param yearMonth 조회할 연월 (YYYY-MM 형식)
     * @return 현재 시퀀스 번호
     */
    @Transactional(readOnly = true)
    public Long getCurrentSequence(String yearMonth) {
        return orderSequenceRepository.findById(yearMonth)
                .map(OrderSequence::getOrderSeq)
                .orElse(0L);
    }

    /**
     * 주문 번호가 유효한 형식인지 검증합니다.
     *
     * @param orderNumber 검증할 주문 번호
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean isValidOrderNumber(String orderNumber) {
        if (orderNumber == null || orderNumber.isEmpty()) {
            return false;
        }

        // ORD-YYYY-MM-XXX 형식인지 검증
        String pattern = "^ORD-\\d{4}-\\d{2}-\\d{3}$";
        return orderNumber.matches(pattern);
    }
}

