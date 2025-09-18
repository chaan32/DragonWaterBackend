package com.dragonwater.backend.Web.Shop.Product.service.implement;

import com.dragonwater.backend.Config.CloudStorage.CloudStorageService;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.AddFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.ImageNotFoundException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.ProductNotFoundException;
import com.dragonwater.backend.Web.Order.repository.OrderItemsRepository;
import com.dragonwater.backend.Web.Shop.Product.domain.*;
import com.dragonwater.backend.Web.Shop.Product.dto.description.ProductDescriptionDto;
import com.dragonwater.backend.Web.Shop.Product.dto.expression.ExpressionAddReqDto;
import com.dragonwater.backend.Web.Shop.Product.dto.expression.ExpressionDeleteReqDto;
import com.dragonwater.backend.Web.Shop.Product.dto.product.*;
import com.dragonwater.backend.Web.Shop.Product.repository.*;
import com.dragonwater.backend.Web.Shop.Product.service.interf.CategoryService;
import com.dragonwater.backend.Web.Shop.Product.service.interf.DescriptionService;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductDisplayService;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import com.dragonwater.backend.Web.Support.Comment.repository.CommentRepository;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.repository.SpecificInquiryRepository;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.dto.product.*;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import jakarta.mail.util.LineOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final DescriptionRepository descriptionRepository;
    private final ExpressionRepository expressionRepository;
    private final ProductImageRepository imageRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final CommentRepository commentRepository;
    private final SpecificInquiryRepository specificInquiryRepository;
    private final SpecializeProductRepository specializeProductRepository;



    // 인터페이스
    private final CloudStorageService cloudStorageService;
    private final ProductDisplayService displayService;
    private final DescriptionService descriptionService;
    private final CategoryService categoryService;
    private final MemberService memberService;


    @Override
    public Products getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Products addProduct(AddProductReqDto dto) {
        try {
            ProductCategories category = categoryService.getSubCategoryById(dto.getCategoryId());
            Products saved = productRepository.save(Products.of(dto, category));

            for (String expression : dto.getExpressions()) {
                expressionRepository.save(ProductExpression.of(saved, expression));
            }

            return saved;
        } catch (Exception e) {
            throw new AddFailedException();
        }
    }

    @Override
    public Queue<ProductResDto> getAllProducts() {
        List<Products> all = productRepository.findAll();
        Queue<ProductResDto> dtos = new LinkedList<>();
        for (Products products : all) {
            dtos.add(ProductResDto.of(products));
        }
        return dtos;
    }

    @Override
    public Queue<ShowProductsResDto> getAllProductsShop() {
        List<Products> all = productRepository.findAll();
        Queue<ShowProductsResDto> dtos = new LinkedList<>();
        for (Products products : all) {
            if (products.getDescription() == null) {
                continue;
            }
            dtos.add(ShowProductsResDto.of(products));
        }
        return dtos;
    }

    @Override
    public ShowProductsDetailInformResDto convertToDetailInformation(Long productId) {
        Products product = getProductById(productId);
        return ShowProductsDetailInformResDto.of(product);
    }

    @Override
    @Transactional
    public boolean addProductDescription(ProductDescriptionDto dto) throws IOException {
        Products product = getProductById(dto.getProductId());
        String thumbnailUrl = cloudStorageService.uploadProductThumbnailImage(dto.getThumbnailImage(), product);
        List<String> imageURLS = cloudStorageService.uploadProductImage(dto.getGalleryImages(), product);

        ProductDescription productDescription = descriptionRepository.save(
                ProductDescription.of(product, dto.getHtmlContents(), thumbnailUrl, dto.getTitle()));

        for (String imageURL : imageURLS) {
            ProductImages productImages = ProductImages.of(productDescription, imageURL);
            imageRepository.save(productImages);
        }

        return true;
    }

    @Override
    public Queue<ProductDetailInformResDto> getAllProductsInform() {
        List<Products> all = productRepository.findAll();
        Queue<ProductDetailInformResDto> dtos = new LinkedList<>();

        for (Products products : all) {
            dtos.add(ProductDetailInformResDto.of(products));
        }
        return dtos;
    }

    @Override
    @Transactional
    public Products editProduct(EditProductReqDto dto) {
        ProductCategories editedCategory = categoryService.getSubCategoryById(dto.getCategoryId());

        Products targetProduct = getProductById(dto.getId());
        targetProduct.edit(dto, editedCategory);
        List<ProductExpression> expressions = targetProduct.getExpressions();


        return targetProduct;
    }

    @Override
    public void updateProductDescription(ProductDescriptionDto dto) throws IOException {
        // 기존 상품 상세 설명을 찾아서 수정하는 로직
        Products product = getProductById(dto.getProductId());

        if (product.getDescription() != null) {
            // 기존 설명 업데이트
            descriptionService.updateDescription(product.getDescription(), dto);
        } else {
            // 설명이 없으면 새로 생성
            addProductDescription(dto);
        }
    }

    @Transactional
    public ProductExpression addEx(ExpressionAddReqDto dto) {
        Products product = getProductById(dto.getProductId());
        ProductExpression newEx = ProductExpression.of(product, dto.getExpression());
        return expressionRepository.save(newEx);
    }


    @Override
    @Transactional
    public void deleteEx(ExpressionDeleteReqDto dto) {
        expressionRepository.deleteById(dto.getExpressionId());
    }


    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Products product = getProductById(productId);

        orderItemsRepository.deleteByProduct(product);
        commentRepository.deleteAll(product.getComments());
        specificInquiryRepository.deleteAll(product.getInquiries());
        cloudStorageService.deleteFile(product);
        descriptionRepository.deleteByProduct(product);

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ImagesResDto getProductImages(Long id) {
        ProductDescription des = descriptionRepository.findByProductId(id);
        return ImagesResDto.of(des);

    }

    @Override
    @Transactional
    public void addImagesToProduct(Long productId, List<MultipartFile> images) throws IOException {
        Products product = getProductById(productId);
        ProductDescription description = product.getDescription();
        for (MultipartFile image : images) {
            String s3Url = cloudStorageService.uploadProductImageBeforeWrite(image, product);
            imageRepository.save(ProductImages.of(description, s3Url));
        }
    }

    @Override
    public void deleteImage(ImageDeleteReqDto dto) {
        imageRepository.deleteById(dto.getImageId());
    }

    @Override
    @Transactional
    public void changeThumbnail(ChangeThumbnailImageReqDto dto) {
        //필요한 거 찾기
        ProductImages productImages = imageRepository.findById(dto.getNewThumbnailId()).orElseThrow(() -> new ImageNotFoundException());
        ProductDescription description = productImages.getDescription();

        // 썸네일이었던 이미지를 그냥 이미지로
        ProductImages th = ProductImages.of(description, dto.getCurrentThumbnailUrl());
        imageRepository.save(th);

        //새로운 URL을 썸네일로
        description.setThumbnailImageS3URL(dto.getNewThumbnailUrl());

        //해당 URL의 데이터는 삭제하기 ...
        imageRepository.deleteById(dto.getNewThumbnailId());
    }

    @Override
    @Transactional
    public void constructMainPagetProduct(ConstructMainPageReqDto dto) {
        displayService.setDisplayItems(dto);
    }

    @Override
    @Transactional(readOnly = true)
    public ConstructMainPageResDto getConstructMainPageProduct() {
        ProductDisplay productDisplay = displayService.currentMainPageProduct();
        return ConstructMainPageResDto.of(productDisplay);
    }

    @Override
    public DisplayProductResDto getDisplayProduct() {
        ProductDisplay productDisplay = displayService.currentMainPageProduct();

        Products newProduct = this.getProductById(productDisplay.getNewProductId());
        Products bestProduct = this.getProductById(productDisplay.getBestProductId());
        Products recommendationProduct = this.getProductById(productDisplay.getRecommendationProductId());

        ShowProductsResDto newP = ShowProductsResDto.of(newProduct);
        ShowProductsResDto bestP = ShowProductsResDto.of(bestProduct);
        ShowProductsResDto recP = ShowProductsResDto.of(recommendationProduct);

        return DisplayProductResDto.of(
                newP, bestP, recP
        );
    }

    @Override
    @Transactional
    public Products getProductByIdWithLock(Long id) {
        return productRepository.findByIdWithPessimisticLock(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Products copyProductById(Long id) {
        Products copyTarget = this.getProductById(id);
        Products copiedProduct = Products.copy(copyTarget);
        return productRepository.save(copiedProduct);
    }

    @Transactional
    @Override
    public void settingHideOption(Long id, Boolean hide) {
        Products product = getProductById(id);
        product.setIsHide(hide);
    }

    @Override
    public List<ProductMinimalInformResDto> getProductList(String term) {
        List<ProductMinimalInformResDto> dtos = new LinkedList<>();
        for (Products products : productRepository.findByNameContaining(term)){
            dtos.add(ProductMinimalInformResDto.of(products));
        }
        return dtos;
    }


    @Transactional
    @Override
    public void injectProduct(Long productId, Long memberId) {
        Members member = memberService.getMemberById(memberId);
        Products product = this.getProductById(productId);
        specializeProductRepository.save(SpecializeProducts.of(product, member));
    }

    @Transactional
    @Override
    public void deleteInjectedProduct(Long specializeProductId) {
        specializeProductRepository.deleteById(specializeProductId);
    }

    @Override
    public Queue<ShowProductsResDto> getSpecializeProduct(Long memberId) {
        return null;
    }
}
