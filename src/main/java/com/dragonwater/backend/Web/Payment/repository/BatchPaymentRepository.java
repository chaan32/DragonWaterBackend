package com.dragonwater.backend.Web.Payment.repository;

import com.dragonwater.backend.Web.Payment.domain.BatchPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchPaymentRepository  extends JpaRepository<BatchPayment, Long> {
    Optional<BatchPayment> findByOrderNumber (String orderNumber);
}
