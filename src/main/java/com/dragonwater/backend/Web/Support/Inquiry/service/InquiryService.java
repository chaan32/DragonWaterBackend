package com.dragonwater.backend.Web.Support.Inquiry.service;

import com.dragonwater.backend.Web.Support.Inquiry.dto.InquiriesResDto;

public interface InquiryService {

    /**
     * 멤버의 모든 문의사항을 가져오는 메소드
     * @param id 멤버를 식별할 수 있는 id 값
     * @return
     */
    InquiriesResDto getMembersInquiries(Long id);
}
