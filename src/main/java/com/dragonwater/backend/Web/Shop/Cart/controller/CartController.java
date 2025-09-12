package com.dragonwater.backend.Web.Shop.Cart.controller;

import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Web.Shop.Cart.dto.CartItemDto;
import com.dragonwater.backend.Web.Shop.Cart.service.CartService;
import com.dragonwater.backend.Web.Shop.Cart.service.CartServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
class CartController {
    private final CartService cartService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<?> addCartItem(@RequestBody CartItemDto dto, HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        log.info("******* productId : {} / quantity : {} ", dto.getProductId(), dto.getQuantity());
        cartService.addOrUpdateCartItemFromCart(memberId, dto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCartItem2(@RequestBody CartItemDto dto, HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        log.info("*****add** productId : {} / quantity : {} ", dto.getProductId(), dto.getQuantity());
        cartService.addOrUpdateCartItemFromWeb(memberId, dto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getCartItems(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        log.info("member Id : {}의 카드 조회", memberId);
        List<CartItemDto> cartItems =
                cartService.getCartItems(memberId);
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long productId, HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        cartService.removeCartItem(memberId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeAllACartAfterOrdering(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        cartService.removeCartAfterOrdering(memberId);
        return ResponseEntity.ok().build();
    }
}
