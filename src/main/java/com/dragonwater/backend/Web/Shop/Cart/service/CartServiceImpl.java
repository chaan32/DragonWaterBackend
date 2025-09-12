package com.dragonwater.backend.Web.Shop.Cart.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.AddFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.DeleteFailedException;
import com.dragonwater.backend.Web.Shop.Cart.domain.CartItems;
import com.dragonwater.backend.Web.Shop.Cart.dto.CartItemDto;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    // 인터페이스 구현 완
    private final MemberService memberService;
    private final ProductService productService;

    // 인터페이스 구현 미완




    @Override
    @Transactional
    public void addOrUpdateCartItemFromCart(Long memberId, CartItemDto cartItemDto) {
        Members member = memberService.getMemberById(memberId);
        Products product = productService.getProductById(cartItemDto.getProductId());
        try {
            member.addOrUpdateCartItem(new CartItems(member, product, cartItemDto.getQuantity()));
        } catch (Exception e) {
            throw new AddFailedException();
        }
    }

    @Override
    @Transactional
    public void addOrUpdateCartItemFromWeb(Long memberId, CartItemDto cartItemDto) {
        Members member = memberService.getMemberById(memberId);
        Products product = productService.getProductById(cartItemDto.getProductId());
        try {
            member.addOneCartItem(new CartItems(member, product, cartItemDto.getQuantity()));
        } catch (Exception e) {
            throw new AddFailedException();
        }
    }

    @Override
    public List<CartItemDto> getCartItems(Long memberId) {
        Members member = memberService.getMemberById(memberId);
        List<CartItems> cartItems = member.getCartItems();
        List<CartItemDto> dtos = new LinkedList<>();
        for (CartItems cartItem : cartItems) {
            dtos.add(CartItemDto.of(cartItem));
        }
        return dtos;
    }

    @Override
    @Transactional
    public void removeCartItem(Long memberId, Long productId) {
        Members member = memberService.getMemberById(memberId);
        try {
            member.removeCartItem(productId);
        } catch (Exception e) {
            throw new DeleteFailedException();
        }
    }

    @Override
    @Transactional
    public void removeCartAfterOrdering(Long memberId) {
        Members member = memberService.getMemberById(memberId);
        try {
            member.removeCartItemAfterOrdering();
        } catch (Exception e) {
            throw new DeleteFailedException();
        }
    }

}
