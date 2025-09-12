package com.dragonwater.backend.Web.Support.Comment.domain;

import com.dragonwater.backend.Web.Shop.Product.dto.product.ReviewReqDto;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments", indexes = {
        @Index(name = "idx_member_product_order", columnList = "member_id, product_id, orderNumber")

})
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Members member;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    private Products product;

    @Column(name = "content", length = 50, nullable = false)
    private String content;

    @Column(name = "orderNumber", nullable = false)
    private String orderNumber;

    @Column(name = "rating")
    private Integer rating;

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

    public static Comments of(ReviewReqDto reviewReqDto, Products product, Members member) {
        return Comments.builder()
                .member(member)
                .product(product)
                .orderNumber(reviewReqDto.getOrderName())
                .content(reviewReqDto.getComment())
                .rating(reviewReqDto.getRating())
                .build();
    }
}
