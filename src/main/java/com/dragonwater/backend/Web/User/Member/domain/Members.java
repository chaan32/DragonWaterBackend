package com.dragonwater.backend.Web.User.Member.domain;

import com.dragonwater.backend.Web.Shop.Cart.domain.CartItems;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import com.dragonwater.backend.Web.Support.Comment.domain.Comments;
import com.dragonwater.backend.Web.Support.Inquiry.General.domain.GeneralInquiries;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "MEMBER_TYPE")
@SuperBuilder
@ToString(exclude = {"orders", "comments", "productsInquiries", "generalInquiries", "cartItems", "claims"})
public abstract class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- 공통으로 갖고 있는 필드 ---
    //로그인 정보
    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;
    @Column(name = "login_pw", nullable = false)
    private String loginPw;

    // 법인(본사, 지점), 개인 회원
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 회원 기본 정보 (공통)
    @Column(name = "name", nullable = false, length = 20)
    private String name; // 법인의 경우는 본사 명
    @Column(name = "phone", nullable = false, length = 13)
    private String phone;
    @Column(name = "email", nullable = false, length = 30)
    private String email;
    @Column(name = "address", nullable = false, length = 40)
    private String address;
    @Column(name = "detailAddress", nullable = false, length = 40)
    private String detailAddress;
    @Column(name = "zip_code")
    private String zipCode;



    // 주문 리스트
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders;

    // 후기 리스트
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments;

    // 특정 제품에 대한 질문 리스트
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductsInquiries> productsInquiries;

    // 전반적인 질문 리스트
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GeneralInquiries> generalInquiries;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claims> claims;

    // 카트 추가하기
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItems> cartItems;

    public Optional<CartItems> findCartItem(Long productId) {
        return this.cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();
    }

    public void addOrUpdateCartItem(CartItems newCartItem) {
        Optional<CartItems> existingCartItem = findCartItem(newCartItem.getProduct().getId());

        if (existingCartItem.isPresent()) {
            CartItems itemToUpdate = existingCartItem.get();
            itemToUpdate.setQuantity(newCartItem.getQuantity());
        } else {
            this.cartItems.add(newCartItem);
        }
    }

    public void addOneCartItem(CartItems newCartItem) {
        Optional<CartItems> existingCartItem = findCartItem(newCartItem.getProduct().getId());

        if (existingCartItem.isPresent()) {
            CartItems itemToUpdate = existingCartItem.get();
            itemToUpdate.setQuantity(itemToUpdate.getQuantity()+1);
        } else {
            this.cartItems.add(newCartItem);
        }
    }

    public void removeCartItem(Long productId) {
        // 상품 ID에 해당하는 장바구니 항목을 찾아 삭제
        this.cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void removeCartItemAfterOrdering() {
        this.cartItems.clear();
    }

    public void updatePassword(String encodedPw) {
        this.loginPw = encodedPw;
    }

    public void updateBranchName(String name) {
        this.name = name;
    }






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



}
