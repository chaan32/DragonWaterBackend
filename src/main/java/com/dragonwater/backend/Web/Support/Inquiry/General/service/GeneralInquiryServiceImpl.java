package com.dragonwater.backend.Web.Support.Inquiry.General.service;

import com.dragonwater.backend.Config.CloudStorage.CloudStorageService;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.InquiryNotFoundException;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.dragonwater.backend.Web.Support.Inquiry.General.domain.GeneralInquiries;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryResDto;
import com.dragonwater.backend.Web.Support.Inquiry.General.repository.GeneralInquiryRepository;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminAnswerReqDto;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneralInquiryServiceImpl implements GeneralInquiryService {
    // 인터페이스 구현 완
    private final CloudStorageService cloudStorageService;
    private final MemberService memberService;



    private final OrderService orderService;
    // 인터페이스 구현 미완

    private final GeneralInquiryRepository inquiryRepository;



    @Override
    public GeneralInquiries addGeneralInquiry(GeneralInquiryReqDto dto, Long memberId, List<MultipartFile> files) throws IOException {
        Members member = memberService.getMemberById(memberId);
        GeneralInquiries generalInquiry;
        if (dto.getOrderId() == null) {
            generalInquiry = GeneralInquiries.of(dto, member, null);
        }else {
            Optional<Orders> orders = orderService.findOptionalOrders(dto.getOrderId());
            generalInquiry = GeneralInquiries.of(dto, member, orders.get());
        }


        if (files != null) {
            List<String> imageUrls = new LinkedList<>();
            for (MultipartFile file : files) {
                imageUrls.add(cloudStorageService.uploadInquiryImage(file));
            }
            generalInquiry.addImageUrls(imageUrls);
        }

        return inquiryRepository.save(generalInquiry);
    }


    @Override
    public Queue<GeneralInquiryResDto> getGeneralInquires(Boolean status) {
        List<GeneralInquiries> in = inquiryRepository.findByIsAnswered(status);
        Queue<GeneralInquiryResDto> dtos = new LinkedList<>();
        for (GeneralInquiries generalInquiries : in) {
            dtos.add(GeneralInquiryResDto.of(generalInquiries));
        }
        return dtos;
    }


    @Override
    public GeneralInquiries uploadAnswer(AdminAnswerReqDto dto) {
        GeneralInquiries inquiry = inquiryRepository.findById(dto.getInquiryId()).orElseThrow(InquiryNotFoundException::new);
        inquiry.enrollAnswer(dto.getAnswer());
        return inquiryRepository.save(inquiry);
    }

}
