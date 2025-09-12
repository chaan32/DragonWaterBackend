package com.dragonwater.backend.Web.Support.Inquiry.General.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.InquiryNotFoundException;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Support.Inquiry.General.domain.GeneralInquiries;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryResDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.dto.AdminAnswerReqDto;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public interface GeneralInquiryService {
    /**
     * 일반적인 문의를 추가하는 메소드
     * @param dto 문의 내용을 담은 DTO
     * @param memberId 멤버를 식별하는 id 값
     * @param files 문의 내용에 필요한 사진들
     * @return
     * @throws IOException 파일을 클라우드 서비스에 올리는데에 실패한 경우
     */
    GeneralInquiries addGeneralInquiry(GeneralInquiryReqDto dto, Long memberId, List<MultipartFile> files) throws IOException;

    /**
     * 승인 상태에 따라서 일반적인 문의를 가져오는 메소드
     * @param status 승인 여부
     * @return
     */
    Queue<GeneralInquiryResDto> getGeneralInquires(Boolean status);

    /**
     * 문의 사항에 대한 답변
     * @param dto 답변을 담은 내용 DTI
     * @return
     */
    GeneralInquiries uploadAnswer(AdminAnswerReqDto dto);
}
