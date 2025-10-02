package com.dragonwater.backend.Web.User.Member.service;

import com.dragonwater.backend.Config.CloudStorage.CloudStorageService;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.MemberNotFoundException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.SignUpFailedException;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.User.Member.domain.*;
import com.dragonwater.backend.Web.User.Member.dto.register.BranchMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.register.HeadQuarterMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.register.IndividualMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.search.HQInformResDto;
import com.dragonwater.backend.Web.User.Member.repository.HQMemberRepository;
import com.dragonwater.backend.Web.User.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    // 인터페이스 구현 완
    @Qualifier("NPCObjectStorage")
    private final CloudStorageService cloudStorageService;




    // 인터페이스 구현 미완
    private final MemberRepository memberRepository;
    private final HQMemberRepository hqMemberRepository;
    private final PasswordEncoder encoder;



    //id로 멤버 객체 얻기
    public Members getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }

    // 로그인 아이디 중복 체크
    public Boolean availableCheckLoginId(String loginId)  {
        return !memberRepository.findByLoginId(loginId).isPresent();
    }

    // 개인 회원 가입
    @Transactional
    public Members registerIndividualMember(IndividualMbRegReqDto dto)  {
        try {
            IndividualMembers newIndividualMember = IndividualMembers.of(dto, encoder.encode(dto.getPassword()));
            return memberRepository.save(newIndividualMember);
        }catch (DataIntegrityViolationException e) {
            log.error("개인 회원가입 실패 - 데이터 제약조건 위반: {}", dto.getId(), e);
            throw new SignUpFailedException("이미 존재하는 아이디입니다.");
        } catch (Exception e) {
            log.error("개인 회원가입 실패: {}", dto.getId(), e);
            throw new SignUpFailedException("회원가입 처리 중 오류가 발생했습니다.");
        }
    }


    // 본사 법인 회원 가입
    @Transactional
    public Boolean registerHeadQuartersMember(HeadQuarterMbRegReqDto dto, MultipartFile businessRegistration)  {
        try{
            String fileName = "headquarters~"+dto.getCompanyName();
            String s3Url = cloudStorageService.uploadRegistrationImage(businessRegistration, fileName);

            HeadQuarterMembers newHeadQuartersMember = HeadQuarterMembers.of(dto, s3Url, encoder.encode(dto.getPassword()));
            memberRepository.save(newHeadQuartersMember);
            return true;
        }catch (DataIntegrityViolationException e) {
            log.error("본사 법인 회원가입 실패 - 데이터 제약조건 위반: {}", dto.getId(), e);
            throw new SignUpFailedException("이미 존재하는 아이디입니다.");
        } catch (Exception e) {
            log.error("본사 법인 회원가입 실패: {}", dto.getId(), e);
            throw new SignUpFailedException("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    // 지점 회원 가입
    @Transactional
    public Boolean registerBranchMember(BranchMbRegReqDto dto, MultipartFile businessRegistration)  {
        try {
            String fileName = "branch~"+dto.getCompanyName()+"~"+dto.getBranchName();
            String s3Url = cloudStorageService.uploadRegistrationImage(businessRegistration, fileName);
            log.info("본사 ID : {}",dto.getHeadquartersId());
            // 본사의 id 값을 통해서 찾는다.
            HeadQuarterMembers headQuarter = hqMemberRepository.findById(dto.getHeadquartersId())
                    .orElseThrow(()-> new SignUpFailedException("본사 정보를 찾을 수 없습니다."));

            BranchMembers newBranchMember = BranchMembers.of(dto, s3Url, encoder.encode(dto.getPassword()), headQuarter);
            headQuarter.addBranch(newBranchMember);
            memberRepository.save(newBranchMember);
            return true;
        } catch (DataIntegrityViolationException e) {
            log.error("지점 법인 회원가입 실패 - 데이터 제약조건 위반: {}", dto.getId(), e);
            throw new SignUpFailedException("이미 존재하는 아이디입니다.");
        } catch (Exception e) {
            log.error("지점 법인 회원가입 실패: {}", dto.getId(), e);
            throw new SignUpFailedException("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    @Override
    public Members getMemberByPhone(String phone) {
        return memberRepository.findByPhone(phone).orElseThrow(()->new MemberNotFoundException(phone));
    }

    @Override
    public Members getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(()->new MemberNotFoundException(email));
    }

    @Override
    @Transactional
    public Boolean registerBranchMember(BranchMbRegReqDto dto) {
        try {

            // 본사의 id 값을 통해서 찾는다.
            HeadQuarterMembers headQuarter = hqMemberRepository.findById(dto.getHeadquartersId())
                    .orElseThrow(()-> new SignUpFailedException("본사 정보를 찾을 수 없습니다."));

            BranchMembers newBranchMember = BranchMembers.of(dto, "제출 X", encoder.encode(dto.getPassword()), headQuarter);
            headQuarter.addBranch(newBranchMember);
            memberRepository.save(newBranchMember);
            return true;
        } catch (DataIntegrityViolationException e) {
            log.error("지점 법인 회원가입 실패 - 데이터 제약조건 위반: {}", dto.getId(), e);
            throw new SignUpFailedException("이미 존재하는 아이디입니다.");
        } catch (Exception e) {
            log.error("지점 법인 회원가입 실패: {}", dto.getId(), e);
            throw new SignUpFailedException("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    @Override
    @Transactional
    public Members resetPassword(Members members, String tempPassword) {
        members.setLoginPw(encoder.encode(tempPassword));
        return memberRepository.save(members);
    }


    // 이름으로 본사 찾아오기
    public List<HQInformResDto> getHQList(String term){
        List<HeadQuarterMembers> containing = hqMemberRepository.findByNameContainingAndApprovalStatus(term, ApprovalStatus.APPROVED);
        List<HQInformResDto> dtos = new LinkedList<>();
        for (HeadQuarterMembers headQuarterMembers : containing) {
            dtos.add(HQInformResDto.of(headQuarterMembers));
        }
        return dtos;
    }

    public Members getMemberByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(()-> new MemberNotFoundException(loginId));
    }

    public boolean canUseEmail(String email) {
        Optional<Members> byEmail = memberRepository.findByEmail(email);
        return byEmail.isEmpty();
    }

    public boolean canUserPhoneNumber(String phone) {
        // BranchMember의 경우에 Phone number -> 매니저 번호 ㅇㅇ
        Optional<Members> byPhone = memberRepository.findByPhone(phone);
        return byPhone.isEmpty();
    }
}
