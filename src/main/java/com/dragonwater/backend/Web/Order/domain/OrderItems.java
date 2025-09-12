package com.dragonwater.backend.Web.Order.domain;

import com.dragonwater.backend.Web.Order.dto.OrderItemDto;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.query.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Products product;


    private BigDecimal perPrice;
    private BigDecimal totalPrice;
    private Integer quantity;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public static OrderItems of(OrderItemDto dto, Orders order, Products product) {
        return OrderItems.builder()
                .order(order)
                .product(product)
                .quantity(dto.getQuantity())
                .perPrice(dto.getPerPrice())
                .totalPrice(dto.getPrice())
                .build();
    }
}
