package com.dragonwater.backend.Web.User.Member.service;

import com.dragonwater.backend.Web.User.Member.dto.corporate.ApproveResponseDto;
import com.dragonwater.backend.Web.User.Member.dto.order.OrderMinimalResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.CorporatesResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.IndividualsResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.MemberInformDto;

import java.util.List;

public interface AdminService {

    /**
     * 법인 승인 대기 회사들 리스트 반환하는 메소드
     * @return
     */
    List<ApproveResponseDto> getPendingStateCorporations();

    /**
     * 법인 승인하기 메소드
     * @param id 법인 식별하는 id 값
     * @return
     */
    Boolean approveCorporates(Long id);

    /**
     * 법인 승인 거절하기 메소드
     * @param id 법인 식별하는 id 값
     * @return
     */
    Boolean rejectCorporates(Long id);

    /**
     * 개인 회원 리스트 반환하는 메소드
     * @return
     */
    List<IndividualsResDto> getIndividualMembersList();

    /**
     * 법인 회원 리스트 반환하는 메소드 -> 단일 법인 - 사용하지 않음
     * @return
     */
    List<CorporatesResDto> getCorporateMembersList();

    /**
     * 모든 멤버를 반환하는 메소드
     * @return
     */
    List<MemberInformDto> getAllMembersList();

    /**
     * 주문 내역을 반환하는 메소드
     * @param memberId 특정 멤버의 id 값
     * @return
     */
     List<OrderMinimalResDto> getOrders(Long memberId);
}
