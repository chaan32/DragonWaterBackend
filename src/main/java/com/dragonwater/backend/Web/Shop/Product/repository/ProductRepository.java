package com.dragonwater.backend.Web.Shop.Product.repository;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.User.Member.domain.ApprovalStatus;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Products p where p.id = :id")
    Optional<Products> findByIdWithPessimisticLock(@Param("id") Long id);

    List<Products> findByNameContaining(String name);
}
