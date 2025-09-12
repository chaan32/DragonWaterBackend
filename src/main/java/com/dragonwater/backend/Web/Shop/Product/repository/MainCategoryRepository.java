package com.dragonwater.backend.Web.Shop.Product.repository;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductMainCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<ProductMainCategories, Long> {
}
