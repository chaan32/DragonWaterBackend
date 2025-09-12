package com.dragonwater.backend.Web.Support.Inquiry.Specific.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.AddFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.EditFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.InquiryNotFoundException;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminAnswerReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminSIQnAResDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.SpecificInquiryQnAReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.SpecificInquiryQnAResDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.repository.SpecificInquiryRepository;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecificInquiryServiceImpl implements SpecificInquiryService {
    // 인터페이스 구현 완
    private final MemberService memberService;
    private final ProductService productService;
    // 인터페이스 구현 미완



    private final SpecificInquiryRepository inquiryRepository;


    @Override
    public Queue<SpecificInquiryQnAResDto> getInquiryByProduct(Long productId) {
        List<ProductsInquiries> inquiries = inquiryRepository.findByProductIdAndAnswered(productId, true);
        Queue<SpecificInquiryQnAResDto> dtos = new LinkedList<>();
        for (ProductsInquiries inquiry : inquiries) {
            dtos.add(SpecificInquiryQnAResDto.of(inquiry));
        }
        return dtos;
    }

    @Override
    public ProductsInquiries addProductInquiry(SpecificInquiryQnAReqDto dto) {
        // 멤버 찾고
        Members member = memberService.getMemberById(dto.getUserId());
        Products product = productService.getProductById(dto.getProductId());
        try {
            ProductsInquiries inquiries = new ProductsInquiries(member, product, dto.getQuestion());
            return inquiryRepository.save(inquiries);
        } catch (Exception e) {
            throw new AddFailedException();
        }
    }

    @Override
    public Queue<AdminSIQnAResDto> getInquiresAtAdminPanelByAnswered(Boolean status) {
        List<ProductsInquiries> inquiries = inquiryRepository.findByAnswered(status);
        Queue<AdminSIQnAResDto> dtos = new LinkedList<>();

        for (ProductsInquiries inquiry : inquiries) {
            dtos.add(AdminSIQnAResDto.of(inquiry));
        }
        return dtos;
    }

    @Override
    public Queue<AdminSIQnAResDto> getInquiresAtAdminPanelByProductId(Long productId) {
        List<ProductsInquiries> inquiries = inquiryRepository.findByProductId(productId);
        Queue<AdminSIQnAResDto> dtos = new LinkedList<>();

        for (ProductsInquiries inquiry : inquiries) {
            dtos.add(AdminSIQnAResDto.of(inquiry));
        }
        return dtos;
    }

    @Override
    public ProductsInquiries uploadAnswer(AdminAnswerReqDto dto) {
        ProductsInquiries inquiry = getProductInquiriesById(dto.getInquiryId());
        try {
            inquiry.enrollAnswer(dto.getAnswer());
            return inquiryRepository.save(inquiry);
        } catch (Exception e) {
            throw new EditFailedException();
        }
    }

    @Override
    public ProductsInquiries getProductInquiriesById(Long id) {
        return inquiryRepository.findById(id).orElseThrow(InquiryNotFoundException::new);
    }

}
