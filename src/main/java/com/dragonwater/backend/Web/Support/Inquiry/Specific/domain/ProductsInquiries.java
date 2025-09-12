package com.dragonwater.backend.Web.Support.Inquiry.Specific.domain;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ProductsInquiries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "product_id")
    private Products product;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Members member;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "response", nullable = false)
    private Boolean answered = false;


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public ProductsInquiries(Members member, Products product, String question) {
        this.member = member;
        this.question = question;
        this.product = product;
    }
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public ProductsInquiries enrollAnswer(String answer) {
        this.answer = answer;
        this.answered = true;
        return this;
    }
}
