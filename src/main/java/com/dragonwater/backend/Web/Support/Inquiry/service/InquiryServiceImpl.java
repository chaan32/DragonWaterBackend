package com.dragonwater.backend.Web.Support.Inquiry.service;

import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import com.dragonwater.backend.Web.Support.Inquiry.General.domain.GeneralInquiries;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import com.dragonwater.backend.Web.Support.Inquiry.dto.ClaimResDto;
import com.dragonwater.backend.Web.Support.Inquiry.dto.GIResDto;
import com.dragonwater.backend.Web.Support.Inquiry.dto.InquiriesResDto;
import com.dragonwater.backend.Web.Support.Inquiry.dto.SIResDto;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    // 인터페이스 구현 완
    private final MemberService memberService;


    @Override
    @Transactional(readOnly = true)
    public InquiriesResDto getMembersInquiries(Long id) {

        Members member = memberService.getMemberById(id);


        List<GIResDto> gi = new LinkedList<>();
        for (GeneralInquiries generalInquiries : member.getGeneralInquiries()) {
            gi.add(GIResDto.of(generalInquiries));
        }

        List<ClaimResDto> cl = new LinkedList<>();
        for (Claims claims : member.getClaims()) {
            cl.add(ClaimResDto.of(claims));
        }

        List<SIResDto> si = new LinkedList<>();
        for (ProductsInquiries inquiries : member.getProductsInquiries()) {
            si.add(SIResDto.of(inquiries));
        }

        return InquiriesResDto.of(gi, si, cl);
    }
}
