package com.dragonwater.backend.Web.Shop.Product.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_description_id", nullable = false)
    @ToString.Exclude
    private ProductDescription description;

    @Column(name = "s3URL", length = 200)
    private String s3URL;
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

    public static ProductImages of(ProductDescription description, String s3URL) {
        return ProductImages.builder()
                .description(description)
                .s3URL(s3URL)
                .build();
    }
}
