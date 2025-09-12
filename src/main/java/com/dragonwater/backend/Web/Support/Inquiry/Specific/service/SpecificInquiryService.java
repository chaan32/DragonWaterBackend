package com.dragonwater.backend.Web.Support.Inquiry.Specific.service;

import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminAnswerReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminSIQnAResDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.SpecificInquiryQnAReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.SpecificInquiryQnAResDto;
import java.util.Queue;

public interface SpecificInquiryService {

    /**
     * 제품에 딸린 문의사항 가져오는 메소드
     * @param productId 제품을 식별할 수 있는 id 값
     * @return
     */
    Queue<SpecificInquiryQnAResDto> getInquiryByProduct(Long productId);

    /**
     * 제품에 문의사항 추가하는 메소드
     * @param dto 공지사항 내용에 대한 DTO
     * @return
     */
    ProductsInquiries addProductInquiry(SpecificInquiryQnAReqDto dto);


    /**
     * 응답에 따라서 관리자 패널에서 문의사항 가져오는 메소드
     * @param status 응답 상태
     * @return
     */
    Queue<AdminSIQnAResDto> getInquiresAtAdminPanelByAnswered(Boolean status);

    /**
     * 관리자 패널에서 제품 문의사항 아이디로 가져오는 메소드
     * @param productId 제품을 식별할 수 있는 id 값
     * @return
     */
    Queue<AdminSIQnAResDto> getInquiresAtAdminPanelByProductId(Long productId);

    /**
     * 관리자 패널에서 답변 달기
     * @param dto 답변을 담은 dto
     * @return
     */
    ProductsInquiries uploadAnswer(AdminAnswerReqDto dto);

    /**
     * 제품에 대한 문의사항 가져오는 메소드
     * @param id 문의 사항을 식별할 수 있는 id 값
     * @return
     */
    ProductsInquiries getProductInquiriesById(Long id);

}
