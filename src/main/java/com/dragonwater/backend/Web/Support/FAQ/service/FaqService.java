package com.dragonwater.backend.Web.Support.FAQ.service;

import com.dragonwater.backend.Web.Support.FAQ.domain.FAQs;
import com.dragonwater.backend.Web.Support.FAQ.domain.FaqCategories;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqCategoryReqDto;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqCategoryResDto;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqReqDto;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqResDto;

import java.util.Queue;

public interface FaqService {

    /**
     * FAQ의 카테고리를 추가하는 메소드
     * @param dto FAQ 카테고리 추가를 위한 DTO
     * @return
     */
    FaqCategories addCategory(FaqCategoryReqDto dto);


    /**
     * FAQ를 추가하는 메소드
     * @param dto FAQ 추가를 위한 DTO
     * @return
     */
    FAQs addFAQ(FaqReqDto dto);

    /**
     * 모든 카테고리를 가져오는 메소드
     * @return
     */
    Queue<FaqCategoryResDto> getAllCategories();

    /**
     * 카테고리 별로 FAQ를 가져오는 메소드
     * @param id
     * @return
     */
    Queue<FaqResDto> getFaqByCategory(Long id);

    /**
     * FAQ를 수정하는 메소드
     * @param dto 수정을 위한 DTO
     * @param id FAQ를 식별하기 위한 id 값
     * @return
     */
    FAQs editFAQ(FaqReqDto dto, Long id);

    /**
     * 카테고리 id를 통해서 카테고리를 가져오기
     * @param id 카테고리를 식별할 수 있는 id 값
     * @return
     */
    FaqCategories getFaqCategoryByCategoryId(Long id);

    /**
     * FAQ를 가져오는 메소드
     * @param id faq를 식별할 수 있는 id 값
     * @return
     */
    FAQs getFAQById(Long id);
}
