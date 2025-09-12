package com.dragonwater.backend.Web.Support.FAQ.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.AddFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.EditFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.FaqCategoryNotFoundException;
import com.dragonwater.backend.Config.Exception.New.Exception.type.FaqNotFoundException;
import com.dragonwater.backend.Web.Support.FAQ.domain.FAQs;
import com.dragonwater.backend.Web.Support.FAQ.domain.FaqCategories;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqCategoryReqDto;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqCategoryResDto;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqReqDto;
import com.dragonwater.backend.Web.Support.FAQ.dto.FaqResDto;
import com.dragonwater.backend.Web.Support.FAQ.repository.FaqCategoryRepository;
import com.dragonwater.backend.Web.Support.FAQ.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;
    private final FaqCategoryRepository faqCategoryRepository;

    @Override
    @Transactional
    public FaqCategories addCategory(FaqCategoryReqDto dto) {
        try {
            FaqCategories faqCategory = FaqCategories.of(dto);
            faqCategoryRepository.save(faqCategory);
            return faqCategory;
        } catch (Exception e) {
            throw new AddFailedException();
        }

    }

    @Override
    @Transactional
    public FAQs addFAQ(FaqReqDto dto) {
        FaqCategories faqCategory = getFaqCategoryByCategoryId(dto.getCategoryId());
        return faqRepository.save(FAQs.of(faqCategory, dto));
    }

    @Override
    public Queue<FaqCategoryResDto> getAllCategories() {
        Queue<FaqCategoryResDto> dtos = new LinkedList<>();
        List<FaqCategories> all = faqCategoryRepository.findAll();
        for (FaqCategories faqCategories : all) {
            dtos.add(FaqCategoryResDto.of(faqCategories));
        }
        return dtos;
    }

    @Override
    public Queue<FaqResDto> getFaqByCategory(Long id) {
        Queue<FaqResDto> dtos = new LinkedList<>();
        for (FAQs faq : faqRepository.findByCategoryId(id)) {
            dtos.add(FaqResDto.of(faq));
        }
        return dtos;
    }

    @Override
    @Transactional
    public FAQs editFAQ(FaqReqDto dto, Long id) {
        FAQs faQs = getFAQById(id);
        try {
            faQs.edit(dto);
            return faQs;
        } catch (Exception e) {
            throw new EditFailedException();
        }
    }

    @Override
    public FaqCategories getFaqCategoryByCategoryId(Long id) {
        return faqCategoryRepository.findById(id)
                .orElseThrow(() -> new FaqCategoryNotFoundException());
    }

    @Override
    public FAQs getFAQById(Long id) {
        return faqRepository.findById(id).orElseThrow(
                () -> new FaqNotFoundException()
        );
    }

}
