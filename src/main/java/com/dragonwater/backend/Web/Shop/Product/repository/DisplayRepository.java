package com.dragonwater.backend.Web.Shop.Product.repository;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDisplay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisplayRepository extends JpaRepository<ProductDisplay, Long> {
    Optional<ProductDisplay> findFirstByOrderByIdAsc();
}
