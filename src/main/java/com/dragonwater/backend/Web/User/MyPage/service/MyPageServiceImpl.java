package com.dragonwater.backend.Web.User.MyPage.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.MemberNotFoundException;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.repository.OrderRepository;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import com.dragonwater.backend.Web.User.MyPage.dto.MPHeadquartersActiveBranches;
import com.dragonwater.backend.Web.User.MyPage.dto.MPHeadquartersDashboardResDto;
import com.dragonwater.backend.Web.User.MyPage.dto.MPOrdersResDto;
import com.dragonwater.backend.Web.User.MyPage.dto.MPUserInformResDto;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import com.dragonwater.backend.Web.User.Member.domain.IndividualMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.dto.update.MembersUpdateReqDto;
import com.dragonwater.backend.Web.User.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    // 인터페이스 구현 완
    private final MemberService memberService;
    // 인터페이시 구현 미완

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    public MPUserInformResDto getUserInform(Long memberId) {
        Members member = memberService.getMemberById(memberId);
        return MPUserInformResDto.of(member);
    }

    public List<MPOrdersResDto> getUserOrders(Long memberId) {
        Members member = memberService.getMemberById(memberId);
        List<Orders> orders = member.getOrders();
        List<MPOrdersResDto> dtos = new LinkedList<>();
        for (Orders order : orders) {
            dtos.add(MPOrdersResDto.of(order));
        }
        return dtos;
    }

    public List<MPOrdersResDto> getUserOrdersV2(Long memberId, Pageable pageable) {
        List<MPOrdersResDto> dtos = new LinkedList<>();
        for (Orders orders : orderRepository.findByMemberId(memberId, pageable)) {
            dtos.add(MPOrdersResDto.of(orders));
        }
        return dtos;
    }

    public MPHeadquartersDashboardResDto getHeadquartersDashboardInform(Long memberId) {
        Members member = memberService.getMemberById(memberId);
        return MPHeadquartersDashboardResDto.of((HeadQuarterMembers) member);
    }

    @Transactional
    public Members updateUser(Long id, MembersUpdateReqDto dto) {
        Members member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다."));

        // 회원의 유형에 따라서 분기
        if (member instanceof IndividualMembers) {
            updateIndividualMember((IndividualMembers) member, dto);
            return member;
        } else if (member instanceof BranchMembers) {
            updateBranchMember((BranchMembers) member, dto);
            return member;
        } else if (member instanceof HeadQuarterMembers) {
            updateHeadquarterMember((HeadQuarterMembers) member, dto);
            return member;
        } else {
            // 혹시 모를 예외 상황에 대비
            throw new IllegalStateException("알 수 없는 회원 유형입니다.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MPHeadquartersActiveBranches getBranchMembers(Long id) {
        HeadQuarterMembers member =(HeadQuarterMembers) memberService.getMemberById(id);
        return MPHeadquartersActiveBranches.of(member);
    }

    // 개인 회원 정보 업데이트 로직
    private void updateIndividualMember(IndividualMembers member, MembersUpdateReqDto dto) {
        // 공통 정보 업데이트
        updateCommonInfo(member, dto);
        Optional.ofNullable(dto.getName()).ifPresent(member::setName);
    }

    // 지점 회원 정보 업데이트 로직
    private void updateBranchMember(BranchMembers member, MembersUpdateReqDto dto) {
        // 공통 정보 업데이트
        updateCommonInfo(member, dto);
        Optional.ofNullable(dto.getName()).ifPresent(member::setName); // 대표자명
        Optional.ofNullable(dto.getCompanyName()).ifPresent(member::setBranchName); // 지점명
    }

    // 본사 회원 정보 업데이트 로직
    private void updateHeadquarterMember(HeadQuarterMembers member, MembersUpdateReqDto dto) {
        // 공통 정보 업데이트
        updateCommonInfo(member, dto);
        Optional.ofNullable(dto.getName()).ifPresent(member::setName); // 대표자명
        Optional.ofNullable(dto.getCompanyName()).ifPresent(member::setName); // 회사명

        List<BranchMembers> branches = member.getBranches();
        for (BranchMembers branch : branches) {
            branch.updateBranchName(dto.getName());
            memberRepository.save(branch);
        }
    }

    // 모든 회원 유형에 공통적으로 적용되는 정보 업데이트 로직
    private void updateCommonInfo(Members member, MembersUpdateReqDto dto) {
        Optional.ofNullable(dto.getEmail()).ifPresent(member::setEmail);
        Optional.ofNullable(dto.getPhone()).ifPresent(member::setPhone);
        Optional.ofNullable(dto.getPostalNumber()).ifPresent(member::setZipCode);
        Optional.ofNullable(dto.getAddress()).ifPresent(member::setAddress);
        Optional.ofNullable(dto.getDetailAddress()).ifPresent(member::setDetailAddress);
    }
}
