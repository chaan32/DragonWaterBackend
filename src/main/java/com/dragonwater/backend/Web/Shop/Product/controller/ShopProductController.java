package com.dragonwater.backend.Web.Shop.Product.controller;

import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Web.Shop.Product.dto.category.ShowCategoriesResDto;
import com.dragonwater.backend.Web.Shop.Product.dto.category.ShowMainAndSubCategoriesResDto;
import com.dragonwater.backend.Web.Shop.Product.dto.product.DisplayProductResDto;
import com.dragonwater.backend.Web.Shop.Product.dto.product.ReviewReqDto;
import com.dragonwater.backend.Web.Shop.Product.dto.product.ShowProductsDetailInformResDto;
import com.dragonwater.backend.Web.Shop.Product.dto.product.ShowProductsResDto;
import com.dragonwater.backend.Web.Shop.Product.service.interf.CategoryService;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import com.dragonwater.backend.Web.Support.Comment.service.CommentService;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.SpecificInquiryQnAReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.service.SpecificInquiryService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Queue;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
@Slf4j
public class ShopProductController {
    // 인터페이스 구현 완
    private final SpecificInquiryService specificInquiryService;
    private final CommentService commentService;
    private final ProductService productService;
    private final CategoryService categoryService;

    // 인터페이스 구현 미완


    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/products")
    public ResponseEntity<Queue<ShowProductsResDto>> getProducts() {
        Queue<ShowProductsResDto> dtos = productService.getAllProductsShop();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/categories")
    public ResponseEntity<Queue<ShowCategoriesResDto>> getCategories() {
        return ResponseEntity.ok(categoryService.findAllCategoriesShop());
    }
    @GetMapping("/all/categories")
    public ResponseEntity<Queue<ShowMainAndSubCategoriesResDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategoriesShopV2());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getDetailAboutProducts(@PathVariable Long productId) {
        ShowProductsDetailInformResDto productDetailInformResDto = productService.convertToDetailInformation(productId);
        return ResponseEntity.ok(productDetailInformResDto);
    }

    @GetMapping("/{productId}/comments")
    public ResponseEntity<?> getCommentByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(commentService.getProductComments(productId));
    }

    @GetMapping("/{productId}/inquiries")
    public ResponseEntity<?> getProductsInquiries(@PathVariable Long productId) {
        return ResponseEntity.ok(specificInquiryService.getInquiryByProduct(productId));
    }

    @PostMapping("/inquiry")
    public ResponseEntity<?> uploadInquiries(@RequestBody SpecificInquiryQnAReqDto dto) {

        specificInquiryService.addProductInquiry(dto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/products/review")
    public ResponseEntity<?> review(@RequestBody ReviewReqDto dto, HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        commentService.addComment(dto, memberId);


        return ResponseEntity.ok().build();
    }

    @GetMapping("/display")
    public ResponseEntity<?> getDisplayProduct() {
        DisplayProductResDto displayProduct = productService.getDisplayProduct();
        return ResponseEntity.ok(displayProduct);
    }

    @GetMapping("/products/specialize")
    public ResponseEntity<?> getSpecializeProducts(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        Queue<ShowProductsResDto> specializeProduct = productService.getSpecializeProduct(memberId);

        return ResponseEntity.ok(specializeProduct);

    }
}
