package com.dragonwater.backend.Web.Shop.Product.repository;

import com.dragonwater.backend.Web.Shop.Product.domain.SpecializeProducts;
import jakarta.persistence.Index;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializeProductRepository extends JpaRepository<SpecializeProducts, Long> {
    List<SpecializeProducts> findByMemberId(Long id);
}
