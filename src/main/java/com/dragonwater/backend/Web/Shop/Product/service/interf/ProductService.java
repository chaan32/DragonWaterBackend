package com.dragonwater.backend.Web.Shop.Product.service.interf;

import com.dragonwater.backend.Web.Shop.Product.domain.*;
import com.dragonwater.backend.Web.Shop.Product.dto.description.ProductDescriptionDto;
import com.dragonwater.backend.Web.Shop.Product.dto.expression.ExpressionAddReqDto;
import com.dragonwater.backend.Web.Shop.Product.dto.expression.ExpressionDeleteReqDto;
import com.dragonwater.backend.Web.Shop.Product.dto.product.*;
import com.dragonwater.backend.Web.User.Member.dto.product.AddProductReqDto;
import com.dragonwater.backend.Web.User.Member.dto.product.EditProductReqDto;
import com.dragonwater.backend.Web.User.Member.dto.product.ProductDetailInformResDto;
import com.dragonwater.backend.Web.User.Member.dto.product.ProductResDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Queue;

public interface ProductService {

    /**
     * 상품 가져오는 메소드
     *
     * @param id 상품을 식별하는 id 값
     * @return
     */
    Products getProductById(Long id);

    /**
     * 상품을 추가하는 메소드
     *
     * @param dto 상품 추가에 필요한 정보가 담긴 DTO
     * @return
     */
    Products addProduct(AddProductReqDto dto);

    /**
     * 모든 제품을 가져오는 메소드
     *
     * @return
     */
    Queue<ProductResDto> getAllProducts();

    /**
     * 홈페이지에서 보여줄 제품의 형태를 가져오는 메소드
     *
     * @return
     */
    Queue<ShowProductsResDto> getAllProductsShop();

    /**
     * 제품의 상세정보를 보여줄 정보
     *
     * @param productId 상품을 식별하는 id 값
     * @return
     */
    ShowProductsDetailInformResDto convertToDetailInformation(Long productId);

    /**
     * 제품의 상세 정보를 추가하는 메소드
     *
     * @param dto 상세정보 추가 DTO
     * @return
     * @throws IOException 이미지 업로드 실패
     */
    boolean addProductDescription(ProductDescriptionDto dto) throws IOException;


    /**
     * 모든 상품의 정보 가져오는 메소드
     *
     * @return
     */
    Queue<ProductDetailInformResDto> getAllProductsInform();

    /**
     * 상품 정보를 수정하는 메소드
     *
     * @param dto 수정할 정보를 담은 DTO
     * @return
     */
    Products editProduct(EditProductReqDto dto);


    /**
     * 상품의 상제 정보를 수정하는 메소드
     *
     * @param dto
     * @throws IOException 이미지 업로드 실패
     */
    void updateProductDescription(ProductDescriptionDto dto) throws IOException;

    /**
     * 제품의 표현을 추가하는 메소드
     *
     * @param dto 표현을 담은 DTO
     * @return
     */
    ProductExpression addEx(ExpressionAddReqDto dto);

    /**
     * 표현을 삭제하는 메소드
     *
     * @param dto
     */
    void deleteEx(ExpressionDeleteReqDto dto);

    /**
     * 상품를 삭제하는 메소드
     *
     * @param productId
     */
    void deleteProduct(Long productId);

    /**
     * 제품의 이미지를 가져오는 메소드
     *
     * @param id 제품을 식별할 수 있는 id 값
     * @return
     */
    ImagesResDto getProductImages(Long id);

    /**
     * 제품의 이미지를 추가하는 메소드
     *
     * @param productId 제품을 식별할 수 있는 id 값
     * @param images    업로드할 제품의 이미지 파일들
     * @throws IOException
     */
    void addImagesToProduct(Long productId, List<MultipartFile> images) throws IOException;

    /**
     * 이미지를 삭제하는 메소드
     *
     * @param dto 이미지 삭제 대상이 들어 있는 DTO
     */
    void deleteImage(ImageDeleteReqDto dto);

    /**
     * 썸네일을 변경하는 메소드
     *
     * @param dto 썸네일 변경에 필요한 정보가 담긴 DTO
     */
    void changeThumbnail(ChangeThumbnailImageReqDto dto);

    /**
     * 메인 페이지에 나타날 제품을 설정하는 메소드
     *
     * @param dto 담긴 DTO
     */
    void constructMainPagetProduct(ConstructMainPageReqDto dto);

    /**
     * 메인 페이지에 나타날 제품에 대한 정보를 구성하는 메소드
     *
     * @return
     */
    ConstructMainPageResDto getConstructMainPageProduct();

    /**
     * 메인 페이지에 나타날 제품에 대한 정보를 가져오는 메소드
     *
     * @return
     */
    DisplayProductResDto getDisplayProduct();

    /**
     * 락을 걸어서 상품을 가져오는 메소드
     *
     * @param id
     * @return
     */
    Products getProductByIdWithLock(Long id);

    /**
     * 이미 있던 상품을 id 값을 받아와서 그래도 상품을 바로 만드는 메소드
     * @param id
     * @return
     */
    Products copyProductById(Long id);
}
