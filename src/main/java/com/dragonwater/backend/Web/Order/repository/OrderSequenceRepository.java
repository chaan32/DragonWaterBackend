package com.dragonwater.backend.Web.Order.repository;

import com.dragonwater.backend.Web.Order.domain.OrderSequence;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderSequenceRepository extends JpaRepository<OrderSequence, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderSequence o where o.yearMonth = :yearMonth")
    Optional<OrderSequence> findByIdWithPessimisticLock(@Param("yearMonth") String yearMonth);
}
