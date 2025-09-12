package com.dragonwater.backend.Web.Order.repository;

import com.dragonwater.backend.Web.Order.domain.BatchPaymentSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchSequenceRepository extends JpaRepository<BatchPaymentSequence, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from BatchPaymentSequence b where b.yearMonth = :yearMonth")
    Optional<BatchPaymentSequence> findByIdWithPessimisticLock(@Param("yearMonth") String yearMonth);
}
