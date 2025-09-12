package com.dragonwater.backend.Config.CloudStorage;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudStorageService {


    /**
     *  사업자 등록증 이미지 업로드 메소드
     * @param businessRegistration : 사업자 등록증 파일
     * @param name : 사업자 이름
     * @return 저장된 링크 URL
     */
    String uploadRegistrationImage(MultipartFile businessRegistration, String name);

    /**
     * 상품의 썸네일 이미지 업로드 메소드
     * @param thumbnailImage : 썸네일 이미지 파일
     * @param products : 상품 정보
     * @return 저장된 링크 URL
     */
    String uploadProductThumbnailImage(MultipartFile thumbnailImage, Products products);

    /**
     * 상품의 갤러리 이미지들 업로드 메소드
     * @param productImages : 갤러리 이미지들 파일
     * @param products : 상품 정보
     * @return URL을 담은 리스트
     */
    List<String> uploadProductImage(List<MultipartFile> productImages, Products products);

    /**
     * 글쓰는 곳에서 이미지 업로드 하는 순간 저장하는 메소드
     * @param file : 이미지 파일
     * @return 저장된 링크 URL
     */
    String uploadProductImageBeforeWrite(MultipartFile file, Products products);

    /**
     * 문의 내용의 이미지 업로드 메소드
     * @param file : 이미지 파일
     * @return 저장된 링크 URL
     */
    String uploadInquiryImage(MultipartFile file);

    /**
     * 환불 관련 이미지 업로드 메소드
     * @param file : 이미지 파일
     * @return 저장된 링크 URL
     */
    String uploadClaimImage(MultipartFile file);

    /**
     * product에 달려 있는 이미지들 모두 삭제하는 메소드
     * @param product 상품
     */
    void deleteFile(Products product);
}
