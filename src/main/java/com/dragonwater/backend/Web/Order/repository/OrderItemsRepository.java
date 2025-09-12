package com.dragonwater.backend.Web.Order.repository;

import com.dragonwater.backend.Web.Order.domain.OrderItems;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository <OrderItems, Long>{
    void deleteByProduct(Products product);
}
