package com.dragonwater.backend.Web.User.Member.controller.Admin;

import com.dragonwater.backend.Config.S3.S3Service;
import com.dragonwater.backend.Web.Shop.Product.domain.ProductCategories;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.dto.description.ProductDescriptionDto;
import com.dragonwater.backend.Web.Shop.Product.service.CategoryService;
import com.dragonwater.backend.Web.Shop.Product.service.DescriptionService;
import com.dragonwater.backend.Web.Shop.Product.service.ProductService;
import com.dragonwater.backend.Web.User.Member.dto.corporate.ApproveResponseDto;
import com.dragonwater.backend.Web.User.Member.dto.product.*;
import com.dragonwater.backend.Web.User.Member.dto.search.CorporatesResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.IndividualsResDto;
import com.dragonwater.backend.Web.User.Member.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final S3Service s3Service;
    private final DescriptionService descriptionService;


    // 승인 대기 중인 업체 리턴
    @GetMapping("/corporate-requests")
    public ResponseEntity<?> getCorporateRequestList() {
        log.info("corporate Request List");
        List<ApproveResponseDto> pendingStateCorporations = adminService.getPendingStateCorporations();
        return ResponseEntity.ok(pendingStateCorporations);
    }

    // 법인 회원 승인
    @PostMapping("/corporate-requests/{id}/approve")
    public ResponseEntity<?> approveCorporate(@PathVariable String id) {
        log.info("corporate Request - approve id - {}", id);
        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        Boolean result = adminService.approveCorporates(Long.parseLong(id));

        if (result) {
            response.put("success", result);
            response.put("message", "승인이 완료 되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 법인 회원 거절
    @PostMapping("/corporate-requests/{id}/reject")
    public ResponseEntity<?> rejectCorporate(@PathVariable String id) {
        log.info("corporate Request - reject id - {}", id);

        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        Boolean result = adminService.rejectCorporates(Long.parseLong(id));

        if (result) {
            response.put("success", result);
            response.put("message", "거절이 완료 되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 회원 가져오기
    @GetMapping("/members/individual")
    public ResponseEntity<?> membersListIndividual() {
        log.info("individual members list ");
        List<IndividualsResDto> list = adminService.getIndividualMembersList();

        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("data", list);
        return ResponseEntity.ok(response);
    }

    // 법인 회원 가져오기
    @GetMapping("/members/corporate")
    public ResponseEntity<?> membersListCorporate() {
        log.info("corporate members list ");
        List<CorporatesResDto> list = adminService.getCorporateMembersList();


        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("data", list);
        return ResponseEntity.ok(response);
    }

//    // 카테고리 추가
//    @PostMapping("/categories/add")
//    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> request) {
//        String name = request.get("name");
//        log.info("새 카테고리 추가 요청: {}", name);
//
//        try {
//
//
//            ProductCategories productCategories = categoryService.addCategory(name);
//
//            Map<String, Object> response = new ConcurrentHashMap<>();
//            response.put("success", true);
//            response.put("message", "카테고리가 성공적으로 추가되었습니다.");
//            response.put("data", productCategories); // 새로 생성된 객체를 반환해주는 것이 좋습니다.
//
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//
//        } catch (Exception e) {
//            log.error("카테고리 추가 중 오류 발생", e);
//            Map<String, Object> response = new ConcurrentHashMap<>();
//            response.put("success", false);
//            response.put("message", "서버 오류로 카테고리를 추가할 수 없습니다.");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }

    //카테고리 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        log.info("카테고리 목록 조회 요청");
        Queue<CategoryResDto> allCategories = categoryService.findAllCategories();
        return ResponseEntity.ok(allCategories);
    }

    // 새로운 상품 등록
    @PostMapping("/products/add")
    public ResponseEntity<?> addProducts(@RequestBody AddProductReqDto dto) {
        log.info("상품 등록 요청 Controller : dto {}", dto);
        Products products = productService.addProduct(dto);
        AddProductResDto addProductResDto = AddProductResDto.of(products);
        addProductResDto.setMessage("상품이 성공적으로 등록되었습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(addProductResDto);
    }

    // 상품 리스트
    @GetMapping("/products")
    public ResponseEntity<?> getProductsList() {
        log.info("상품 목록 조회");
        Queue<ProductResDto> dtos = productService.getAllProducts();
        return ResponseEntity.ok(dtos);
    }

    // 기존 상품의 상세 설명 등록/수정
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

    // 이미지 실시간 저장
    @PostMapping(value = "/products/upload/image")
    public ResponseEntity<?> uploadImage(@RequestParam(value = "image", required = false) MultipartFile image) throws IOException {
        String imageUrl = s3Service.uploadProductImageBeforeWrite(image);
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);

        log.info("이미지 업로드 성공: {}", imageUrl);
        return ResponseEntity.ok(response);
    }

    // 상세내용 불러오기
    @GetMapping("/products/{productId}/content")
    public ResponseEntity<?> getContent(@PathVariable Long productId) {
        Products product = productService.getProductById(productId);
        if (product.getDescription() == null) {
            return ResponseEntity.ok(null);
        }
        DescriptionResDto content = descriptionService.getContent(product.getDescription());
        return ResponseEntity.ok(content);
    }


    // 상품 목록 가져 오기 EDITPAGE
    @GetMapping("/products/edit")
    public ResponseEntity<?> getAllProducts() {
        Queue<ProductDetailInformResDto> dtos = productService.getAllProductsInform();
        return ResponseEntity.ok(dtos);
    }

    // 상품 수정하기
    @PutMapping("/products/edit/{id}")
    public ResponseEntity<?> editProduct(@PathVariable Long id, @RequestBody EditProductReqDto dto) {
        dto.setId(id);
        log.info("dto : {}", dto.toString());
        Products editedProduct = productService.editProduct(dto);
        return ResponseEntity.ok(editedProduct);
    }
}
