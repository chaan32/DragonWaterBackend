package com.dragonwater.backend.Web.Shop.Product.repository;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDescription;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionRepository extends JpaRepository<ProductDescription, Long> {

    ProductDescription findByProductId(Long id);

    void deleteByProduct(Products product);
}
