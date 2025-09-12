package com.dragonwater.backend.Web.Shop.Product.domain;

import com.dragonwater.backend.Web.Shop.Cart.domain.CartItems;
import com.dragonwater.backend.Web.Support.Comment.domain.Comments;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import com.dragonwater.backend.Web.User.Member.dto.product.AddProductReqDto;
import com.dragonwater.backend.Web.User.Member.dto.product.EditProductReqDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 메인 카테고리, 서브 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private ProductCategories category;

    @OneToOne(mappedBy = "product")
    @ToString.Exclude
    private ProductDescription description;

    // 맵핑 구조
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductsInquiries> inquiries;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> cartItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductExpression> expressions;


    // 상품에 대한 진짜 정보
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "customer_price", nullable = false)
    private Integer customerPrice;
    @Column(name = "business_price", nullable = false)
    private Integer businessPrice;
    @Column(name = "discount_price", nullable = false)
    private Integer discountPrice;
    @Column(name = "discount_percent")
    private Integer discountPercent;
    @Column(name = "stock", nullable = false)
    private Integer stock;

    // 추천 상품 여부
    @Column(name = "is_best", nullable = false)
    private Boolean isBest;
    @Column(name = "is_recommendation", nullable = false)
    private Boolean isRecommendation;
    @Column(name = "is_new", nullable = false)
    private Boolean isNew;

    // ㅍㅕㅇㅈㅓㅁ
    private Float rating;

    // 판매 상태 (판매중, 품절, 판매 중단)
    @Enumerated(EnumType.STRING)
    @Column(name = "sales_status", nullable = false)
    private SalesStatus salesStatus;

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

    public static Products of(AddProductReqDto dto, ProductCategories category) {
        return Products.builder()
                .customerPrice(dto.getCustomerPrice())
                .businessPrice(dto.getBusinessPrice())
                .name(dto.getName())
                .discountPercent(dto.getDiscountPercent())
                .discountPrice(dto.getDiscountPrice())
                .category(category)
                .isRecommendation(false)
                .isBest(false)
                .isNew(false)
                .stock(dto.getStock())
                .salesStatus(SalesStatus.ON_SALE)
                .build();
    }

    public static Products copy(Products products) {
        return Products.builder()
                .customerPrice(products.getCustomerPrice())
                .businessPrice(products.getBusinessPrice())
                .name(products.getName()+"-cpPt")
                .discountPercent(products.getDiscountPercent())
                .discountPrice(products.getDiscountPrice())
                .category(products.getCategory())
                .isRecommendation(false)
                .isBest(false)
                .isNew(false)
                .stock(products.getStock())
                .salesStatus(SalesStatus.ON_SALE)
                .build();
    }

    public Products edit(EditProductReqDto dto, ProductCategories category) {
        this.name = dto.getName();
        this.customerPrice = dto.getCustomerPrice();
        this.businessPrice = dto.getBusinessPrice();
        this.discountPrice = dto.getDiscountPrice();
        this.discountPercent = dto.getDiscountPercent();
        this.stock = dto.getStock();
        this.category = category;
        this.isNew = dto.getIsNew();
        this.isRecommendation = dto.getIsRecommendation();
        this.isBest = dto.getIsBest();
        if (dto.getStatus().equals("판매 중")) {
            this.salesStatus = SalesStatus.ON_SALE;
        } else if (dto.getStatus().equals("판매 중단")) {
            this.salesStatus = SalesStatus.DISCONTINUED;
        } else {
            this.salesStatus = SalesStatus.SOLD_OUT;
        }

        return this;
    }

    public void addComment(Comments comments) {
        this.comments.add(comments);
    }

    public void calculateRating(Comments comment) {
        if (this.comments.size() == 0) {
            this.rating =  Float.valueOf(comment.getRating());
        }
        else if (this.comments.size() > 0) {
            Float sum = comments.size() * this.rating;
            Float newSum = sum + Float.valueOf(comment.getRating());
            this.rating = newSum / (this.comments.size()+1);
        }
    }

    public void setBest(boolean f) {
        this.isBest = f;
    }
    public void setRecommendation(boolean f){
        this.isRecommendation = f;
    }
    public void setNew(boolean f){
        this.isNew = f;
    }

    public boolean stockCheck(Integer quantity) {
        if (stock < quantity) {
            return false;
        }
        return true;
    }

    public boolean order(Integer quantity) {
        if (this.stockCheck(quantity)) {
            this.stock-=quantity;
            if (this.stock == 0) {
                this.salesStatus = SalesStatus.SOLD_OUT;
            }
            return true;
        }
        else {
            return false;
        }
    }

    public void addStock(Integer quantity) {
        this.stock = quantity;
        this.salesStatus = SalesStatus.ON_SALE;
    }
}
