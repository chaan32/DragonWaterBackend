package com.dragonwater.backend.Web.User.Member.service;

import com.dragonwater.backend.Web.User.Member.domain.*;
import com.dragonwater.backend.Web.User.Member.dto.register.BranchMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.register.HeadQuarterMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.register.IndividualMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.search.HQInformResDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService {

    /**
     * Members 객체를 얻는 메소드
     * @param id members를 식별할 수 있는 id 값
     * @return
     */
    Members getMemberById(Long id);

    /**
     * 핸드폰 번호를 통해서 Members 객체를 얻는 메소드
     * @param phone
     * @return
     */
    Members getMemberByPhone(String phone);

    /**
     * 이메일을 통해서 Members 객체를 얻는 메소드
     * @param email
     * @return
     */
    Members getMemberByEmail(String email);

    // 로그인 아이디 중복 체크

    /**
     * 로그인 아이디 중복 체크 -> 이게 왜 여깄지? auth에 있어야 할 거 같은데 ㅇㅇ...
     * @param loginId 로그인 아이디
     * @return
     */
    Boolean availableCheckLoginId(String loginId);


    /**
     * 개인 회원 가입을 하는 메소드
     * @param dto 개인 회원 가입에 필요한 DTO 정보
     * @return
     */
    Members registerIndividualMember(IndividualMbRegReqDto dto);


    /**
     * 본사 회원 가입을 하는 메소드
     * @param dto 본사 회원 가입에 필요한 DTO 정보
     * @param businessRegistration 사업자 등록증 이미지 파일
     * @return
     */
    Boolean registerHeadQuartersMember(HeadQuarterMbRegReqDto dto, MultipartFile businessRegistration);

    /**
     * 지점 회원 가입을 하는 메소드 사업자등록증 제출 O
     * @param dto 지점 회원 가입에 필요한 DTO 정보
     * @param businessRegistration
     * @return
     */
    Boolean registerBranchMember(BranchMbRegReqDto dto, MultipartFile businessRegistration);

    /**
     * 지점 회원 가입을 하는 메소드 사업자등록증 제출 X
     * @param dto 지점 회원 가입에 필요한 DTO 정보
     * @return
     */
    Boolean registerBranchMember(BranchMbRegReqDto dto);



    /**
     * 이름으로 본사 찾아오기
     * @param term 글자 -> 본사 이름이 이에 해당하면 가져오기 예) 스 -> "스타벅스" / 커 -> "커피빈", "메가커피", "컴포즈커피" 등
     * @return
     */
    List<HQInformResDto> getHQList(String term);

    /**
     * 로그인 아이디로 멤버 객체 가져오기
     * @param loginId 로그인 아이디
     * @return
     */
    Members getMemberByLoginId(String loginId);

    /**
     * 회원 가입 메일 중복 체크
     * @param email 요청 이메일
     * @return
     */
    boolean canUseEmail(String email);

    /**
     * 회원 가입 핸드폰 번호 중복 체크
     * @param phone 요청 핸드폰 번호
     * @return
     */
    boolean canUserPhoneNumber(String phone);

    /**
     * 임시 비밀번호로 초기화하는 메소드
     * @param members 멤버 객체
     * @return
     */
    Members resetPassword(Members members, String tempPassword);
}
