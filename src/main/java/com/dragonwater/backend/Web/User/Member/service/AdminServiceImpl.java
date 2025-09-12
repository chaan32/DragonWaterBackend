package com.dragonwater.backend.Web.User.Member.service;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.dragonwater.backend.Web.User.Member.domain.*;
import com.dragonwater.backend.Web.User.Member.dto.corporate.ApproveResponseDto;
import com.dragonwater.backend.Web.User.Member.dto.order.OrderMinimalResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.CorporatesResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.IndividualsResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.MemberInformDto;
import com.dragonwater.backend.Web.User.Member.repository.BRMemberRepository;
import com.dragonwater.backend.Web.User.Member.repository.HQMemberRepository;
import com.dragonwater.backend.Web.User.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {
    // 인터페이스 구현 완
    private final MemberService memberService;
    private final OrderService orderService;


    // 인터페이스 구현 미완


    private final HQMemberRepository hqMemberRepository;
    private final BRMemberRepository brMemberRepository;
    private final MemberRepository memberRepository;



    public List<ApproveResponseDto> getPendingStateCorporations() {
        List<HeadQuarterMembers> pendingListHQ = hqMemberRepository.findByApprovalStatus(ApprovalStatus.PENDING);
        List<BranchMembers> pendingListBR = brMemberRepository.findByApprovalStatus(ApprovalStatus.PENDING);
        List<ApproveResponseDto> dtos = new LinkedList<>();
        for (HeadQuarterMembers hqMember : pendingListHQ) {
            dtos.add(ApproveResponseDto.of(hqMember));
        }
        for (BranchMembers brMembers : pendingListBR) {
            dtos.add(ApproveResponseDto.of(brMembers));
        }
        return dtos;
    }

    @Transactional
    public Boolean approveCorporates(Long id) {
        Members memberById = memberService.getMemberById(id);

        // 1. 본사 회원인 경우
        if (memberById instanceof HeadQuarterMembers) {
            HeadQuarterMembers member = (HeadQuarterMembers) memberById;
            return member.approveStatus();
        }
        // 2. 지점 회원인 경우
        else if (memberById instanceof BranchMembers) {
            BranchMembers member = (BranchMembers) memberById;
            return member.approveStatus();
        }

        return false;
    }

    @Transactional
    public Boolean rejectCorporates(Long id) {
        Members memberById = memberService.getMemberById(id);

        // 1. 본사 회원인 경우
        if (memberById instanceof HeadQuarterMembers) {
            HeadQuarterMembers member = (HeadQuarterMembers) memberById;
            return member.rejectStatus();
        }
        // 2. 지점 회원인 경우
        else if (memberById instanceof BranchMembers) {
            BranchMembers member = (BranchMembers) memberById;
            return member.rejectStatus();
        }
        return false;
    }

    @Transactional
    public List<IndividualsResDto> getIndividualMembersList() {
        List<Members> individualMembers = memberRepository.findByRole(Role.INDIVIDUAL);
        List<IndividualsResDto> dtos = new LinkedList<>();

        for (Members individualMember : individualMembers) {
            dtos.add(IndividualsResDto.of((IndividualMembers) individualMember));
        }
        return dtos;
    }

    @Transactional
    public List<CorporatesResDto> getCorporateMembersList(){
        List<Members> branch = memberRepository.findByRole(Role.BRANCH);
        List<Members> headquarters = memberRepository.findByRole(Role.HEADQUARTERS);
        List<CorporatesResDto> dtos = new LinkedList<>();

        for (Members members : headquarters) {
            HeadQuarterMembers headQuarterMembers = (HeadQuarterMembers) members;
            if (headQuarterMembers.getApprovalStatus() == ApprovalStatus.APPROVED) {
                dtos.add(CorporatesResDto.of(headQuarterMembers));
            }
        }

        for (Members members : branch) {
            BranchMembers branchMembers = (BranchMembers) members;
            if (branchMembers.getApprovalStatus() == ApprovalStatus.APPROVED){
                dtos.add(CorporatesResDto.of(branchMembers));
            }


        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public List<MemberInformDto> getAllMembersList() {
        List<MemberInformDto> dtos = new LinkedList<>();
        for (Members members : memberRepository.findAll()) {
            MemberInformDto dto = MemberInformDto.of(members);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public List<OrderMinimalResDto> getOrders(Long memberId) {

        List<Orders> orders = orderService.findOrderByMemberId(memberId);
        List<OrderMinimalResDto> ordersDto = new LinkedList<>();
        for (Orders order : orders) {
            ordersDto.add(OrderMinimalResDto.of(order));
        }
        return ordersDto;
    }

}
