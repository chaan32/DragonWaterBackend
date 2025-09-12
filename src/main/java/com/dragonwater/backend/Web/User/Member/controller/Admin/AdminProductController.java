package com.dragonwater.backend.Web.User.Member.controller.Admin;

import com.dragonwater.backend.Config.CloudStorage.CloudStorageService;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductCategories;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductExpression;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductMainCategories;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.dto.description.ProductDescriptionDto;
import com.dragonwater.backend.Web.Shop.Product.dto.expression.ExpressionAddReqDto;
import com.dragonwater.backend.Web.Shop.Product.dto.expression.ExpressionAddResDto;
import com.dragonwater.backend.Web.Shop.Product.dto.expression.ExpressionDeleteReqDto;
import com.dragonwater.backend.Web.Shop.Product.dto.product.*;
import com.dragonwater.backend.Web.Shop.Product.service.interf.CategoryService;
import com.dragonwater.backend.Web.Shop.Product.service.interf.DescriptionService;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import com.dragonwater.backend.Web.User.Member.dto.product.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    // 인터페이스 구현 완
    private final ProductService productService;
    private final DescriptionService descriptionService;
    private final CategoryService categoryService;


    // 인터페이스 구현 미완





    private final CloudStorageService cloudStorageService;



    /**
     *        카테고리 관련 API들 (메인, 서브 카테고리)
     *    1) 메인 카테고리 추가 - addMainCategory
     *    2) 서브 카테고리 추가 - addSubCategory
     *    3) 메인 카테고리 수정 - putMainCategory
     *    4) 서브 카테고리 수정 - putSubCategory
     *    5) 메인 카테고리 삭제 - deleteMainCategory
     *    6) 서브 카테고리 삭제 - deleteSubCategory
     *    7) 메인 카테고리 조회 - getAllMainCategories
     *    8) 서브 카테고리 조회 - getAllSubCategories
     *    9) 메인 카테고리를 통해서 서브 카테고리 조회 - getSubCategories
     */

    @PostMapping("/main/categories/add")
    public ResponseEntity<?> addMainCategory(@RequestBody AddCategoryReqDto dto) {
        String name = dto.getName();
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("카테고리 이름은 비어있을 수 없습니다.");
        }
        try {
            ProductMainCategories productMainCategories = categoryService.addMainCategory(name);
            Map<String, Object> response = new ConcurrentHashMap<>();
            response.put("success", true);
            response.put("message", "카테고리가 성공적으로 추가되었습니다.");
            response.put("data", productMainCategories);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("카테고리 추가 중 오류 발생", e);
            Map<String, Object> response = new ConcurrentHashMap<>();
            response.put("success", false);
            response.put("message", "서버 오류로 카테고리를 추가할 수 없습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/sub/categories/add")
    public ResponseEntity<?> addSubCategory(@RequestBody AddCategoryReqDto dto) {
        String name = dto.getName();
        Long mainCategoryId = dto.getId();
        ProductMainCategories mainCategory = categoryService.findMainCategory(mainCategoryId);
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("카테고리 이름은 비어있을 수 없습니다.");
        }
        try {
            ProductCategories productCategories = categoryService.addCategory(mainCategory, name);
            Map<String, Object> response = new ConcurrentHashMap<>();
            response.put("success", true);
            response.put("message", "카테고리가 성공적으로 추가되었습니다.");
            response.put("data", productCategories);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("카테고리 추가 중 오류 발생", e);
            Map<String, Object> response = new ConcurrentHashMap<>();
            response.put("success", false);
            response.put("message", "서버 오류로 카테고리를 추가할 수 없습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/main/categories/{id}/{name}")
    public ResponseEntity<?> putMainCategory(@PathVariable Long id, @PathVariable String name) {
        categoryService.updateCategory(id, name, true);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/sub/categories/{id}/{name}")
    public ResponseEntity<?> putSubCategory(@PathVariable Long id, @PathVariable String name) {
        categoryService.updateCategory(id, name, false);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/main/categories/{id}")
    public ResponseEntity<?> deleteMainCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id, true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sub/categories/{id}")
    public ResponseEntity<?> deleteSubCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id, false);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sub/categories")
    public ResponseEntity<?> getAllSubCategories() {
        log.info("서브 카테고리 목록 조회 요청");
        Queue<CategoryResDto> allCategories = categoryService.findAllSubCategories();
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/sub/categories/{mainCategoryId}")
    public ResponseEntity<?> getSubCategories(@PathVariable Long mainCategoryId) {
        Queue<CategoryResDto> allCategories = categoryService.findSubCategoriesById(mainCategoryId);
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/main/categories")
    public ResponseEntity<?> getAllMainCategories() {
        Queue<CategoryResDto> allCategories = categoryService.findAllMainCategories();
        return ResponseEntity.ok(allCategories);
    }


    /**
     *        상품 관련 API들
     *    1) 새로운 상품 등록 - addProducts
     *    2) 상품 리스트 반환 - getProductsList
     *    3) 기존의 상품 상세 정보 등록 - uploadDetail
     *    4) 상품의 상세 컨텐츠 정보 제공 - getContent
     *    5) 상품 리스트 반환 (EditPage) - getAllProducts
     *    6) 상품 수정하기 - editProduct
     *    7) 표현 추가하기 (EditPage) - addEx
     *    8) 표현 삭제하기 (EditPage) - deleteEx
     *    9) 상품 삭제하기 - deleteProduct
     *    10) 메인 화면에 나타낼 상품 설정하기 - constructMainPage
     *    11) 메인 화면에 나타낼 상품 정보제공 - getMainPage
     */


    @PostMapping("/products/add")
    public ResponseEntity<?> addProducts(@RequestBody AddProductReqDto dto) {
        log.info("상품 등록 요청 Controller : dto {}", dto);
        Products products = productService.addProduct(dto);
        AddProductResDto addProductResDto = AddProductResDto.of(products);
        addProductResDto.setMessage("상품이 성공적으로 등록되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(addProductResDto);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProductsList() {
        log.info("상품 목록 조회");
        Queue<ProductResDto> dtos = productService.getAllProducts();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping(value = "/products/content", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadDetail(
            @RequestParam("productId") Long productId,
            @RequestParam("title") String title,
            @RequestParam("htmlContent") String htmlContents,
            @RequestParam(value = "mode") String mode, // "create" 또는 "edit" 구분
            @RequestParam(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
            @RequestParam(value = "thumbnailImageUrl", required = false) String thumbnailImageUrl, // 기존 썸네일 URL
            @RequestParam(value = "galleryImages", required = false) List<MultipartFile> galleryImages,
            @RequestParam(value = "editorImages", required = false) List<MultipartFile> editorImages) throws IOException {

        log.info("상품 상세 설명 {}모드 - productId: {}", mode, productId);

        ProductDescriptionDto dto;

        if ("create".equals(mode)) {
            // 신규 등록: 모든 파일을 새로 저장
            dto = ProductDescriptionDto.of(productId, title, htmlContents, thumbnailImage, galleryImages);
            productService.addProductDescription(dto);

        } else if ("edit".equals(mode)) {
            // 수정 모드
            if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
                // 새 썸네일 파일이 있으면 새로 업로드
                dto = ProductDescriptionDto.of(productId, title, htmlContents, thumbnailImage, galleryImages);
            } else if (thumbnailImageUrl != null && !thumbnailImageUrl.trim().isEmpty()) {
                // 기존 썸네일 URL 유지
                dto = ProductDescriptionDto.ofWithExistingThumbnail(productId, title, htmlContents, thumbnailImageUrl, galleryImages);
            } else {
                // 썸네일 없이 수정
                dto = ProductDescriptionDto.of(productId, title, htmlContents, null, galleryImages);
            }

            productService.updateProductDescription(dto); // 수정용 서비스 메소드 호출

        } else {
            return ResponseEntity.badRequest().body("Invalid mode parameter");
        }

        Map<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("message", "create".equals(mode) ? "상품 상세 설명이 등록되었습니다." : "상품 상세 설명이 수정되었습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{productId}/content")
    public ResponseEntity<?> getContent(@PathVariable Long productId) {
        Products product = productService.getProductById(productId);
        if (product.getDescription() == null) {
            return ResponseEntity.ok(null);
        }
        DescriptionResDto content = descriptionService.getContent(product.getDescription());
        return ResponseEntity.ok(content);
    }

    @GetMapping("/products/edit")
    public ResponseEntity<?> getAllProducts() {
        Queue<ProductDetailInformResDto> dtos = productService.getAllProductsInform();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/products/edit/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id, @RequestBody EditProductReqDto dto) {
        dto.setId(id);
        Products editedProduct = productService.editProduct(dto);
        return ResponseEntity.ok(editedProduct);
    }

    @PostMapping("/expressions")
    public ResponseEntity<?> addEx(@RequestBody ExpressionAddReqDto dto) {
        ProductExpression productExpression = productService.addEx(dto);
        return ResponseEntity.ok(ExpressionAddResDto.of(productExpression));
    }

    @DeleteMapping("/expressions")
    public ResponseEntity<?> deleteEx(@RequestBody ExpressionDeleteReqDto dto) {
        productService.deleteEx(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/main-page-products")
    public ResponseEntity<?> constructMainPage(@RequestBody ConstructMainPageReqDto dto) {
        log.info("main Page Dto : {}", dto);
        productService.constructMainPagetProduct(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/main-page-products")
    public ResponseEntity<?> getMainPage() {
        ConstructMainPageResDto dto = productService.getConstructMainPageProduct();
        return ResponseEntity.ok(dto);
    }


    /**
     *        추가 유틸 API들
     *    1) 이미지 실시간 저장 - uploadImage
     *    2) 상품의 이미지들 가져오기 - getProductsImages
     *    3) 이미지 삭제하기 - deleteImage
     *    4) 썸네일 변경하기 - uploadProductImages
     */
    @PostMapping(value = "/products/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "productId") Long productId) throws IOException {
        Products product = productService.getProductById(productId);
        String imageUrl = cloudStorageService.uploadProductImageBeforeWrite(image, product);
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);

        log.info("이미지 업로드 성공: {}", imageUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{id}/images")
    public ResponseEntity<?> getProductsImages(@PathVariable Long id) {
        ImagesResDto productImages = productService.getProductImages(id);
        return ResponseEntity.ok(productImages);
    }

    @DeleteMapping("/products/images")
    public ResponseEntity<?> deleteImage(@RequestBody ImageDeleteReqDto dto) {
        productService.deleteImage(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/products/images/thumbnail")
    public ResponseEntity<?> changeThumbnail(@RequestBody ChangeThumbnailImageReqDto dto) {
        productService.changeThumbnail(dto);
        return ResponseEntity.ok().build();
    }


    // 이미지 업로드 (수정하는 곳에서 -> 이거 그냥 공통으로 쓰면 될거 같으아) -??????
    @PostMapping(value = "/products/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProductImages(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("productId") Long productId) {

        try {
            // ProductService에 이미지 추가 로직 호출
            productService.addImagesToProduct(productId, images);
            return ResponseEntity.ok().body("이미지 업로드 성공");
        } catch (IOException e) {
            log.error("이미지 업로드 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드 실패");
        }
    }

    @PostMapping("/products/add/copy")
    public ResponseEntity<?> productCopy(@RequestBody ProductCopyReqDto dto) {
        Products copied = productService.copyProductById(dto.getProductId());
        return ResponseEntity.ok().body(copied);
    }
}
