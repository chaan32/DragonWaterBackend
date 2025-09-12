package com.dragonwater.backend.Web.Shop.Cart.domain;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Members member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Products product;

    private int quantity; // 장바구니에 담긴 상품의 수량

    public CartItems(Members member, Products product, Integer quantity) {
        this.member = member;
        this.product = product;
        this.quantity = quantity;
    }
}
