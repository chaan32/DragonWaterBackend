package com.dragonwater.backend.Web.Support.Claim.service;

import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import com.dragonwater.backend.Web.Support.Claim.dto.ClaimResDto;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryReqDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClaimService {

    /**
     * 클레임을 접수하는 메소드
     * @param dto 클레임 내용
     * @param files 클레임 접수에 넣은 사진
     * @param memberId 클레임 접수를 한 member의 값
     * @return
     */
    Claims addClaims(GeneralInquiryReqDto dto, List<MultipartFile> files, Long memberId);


    /**
     * 모든 클레임을 가져오는 메소드
     * @return
     */
    List<ClaimResDto> getClaims();

    /**
     * 클레임을 답변하는 메소드
     * @param claimId 클레임을 식별할 수 있는 id 값
     * @param status 클레임 승인 여부
     * @param reason 클레임 거절의 경우 거절의 이유
     */
    @Transactional
    void updateClaims(Long claimId, String status, String reason);

    /**
     * 클레임을 가져오는 메소드
     * @param id 클레임을 식별할 수 있는 id 값
     * @return
     */
    Claims getClaimById(Long id);
}
