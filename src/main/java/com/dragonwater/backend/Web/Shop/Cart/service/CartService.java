package com.dragonwater.backend.Web.Shop.Cart.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.AddFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.DeleteFailedException;
import com.dragonwater.backend.Web.Shop.Cart.domain.CartItems;
import com.dragonwater.backend.Web.Shop.Cart.dto.CartItemDto;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

public interface CartService {


    /**
     * 카트에서 수량 바꾸기 메소드
     * @param memberId 멤버를 식별할 수 있는 id 값
     * @param cartItemDto 카트 아이템 정보
     */
    void addOrUpdateCartItemFromCart(Long memberId, CartItemDto cartItemDto);

    /**
     * 웹에서 수량 바꾸기 메소드
     * @param memberId 멤버를 식별할 수 있는 id 값
     * @param cartItemDto 카트 아이템 정보
     */
    void addOrUpdateCartItemFromWeb(Long memberId, CartItemDto cartItemDto);


    /**
     * 카트 정보를 가져오는 메소드
     * @param memberId 멤버를 식별할 수 있는 id 값
     * @return
     */
    List<CartItemDto> getCartItems(Long memberId);

    /**
     * 장바구니에 있는 정보를 삭제하는 메소드
     * @param memberId 멤버를 식별할 수 있는 id 값
     * @param productId 상품을 식별할 수 있는 id 값
     */
    void removeCartItem(Long memberId, Long productId);

    /**
     * 주문을 하게 되면 장바구니를 모두 삭제하는 메소드
     * @param memberId 멤버를 식별할 수 있는 id 값
     */
    void removeCartAfterOrdering(Long memberId);
}
