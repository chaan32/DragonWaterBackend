package com.dragonwater.backend.Web.Shop.Product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private Products product;

    @Column(name = "title")
    private String title; // 제목 필드 추가

    @Lob
    private String description;

    @Column(name = "thumbnail_image_s3_url", nullable = false)
    private String thumbnailImageS3URL;

    @OneToMany(mappedBy = "description", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImages> galleryImages;


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


    public static ProductDescription of(Products product, String htmlContent, String thumbnailImageS3URL, String title) {
        return ProductDescription.builder()
                .product(product)
                .title(title)
                .description(htmlContent)
                .thumbnailImageS3URL(thumbnailImageS3URL)
                .build();
    }
}
