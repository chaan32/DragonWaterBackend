package com.dragonwater.backend.Web.User.MyPage.service;


import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.dto.update.MembersUpdateReqDto;
import com.dragonwater.backend.Web.User.MyPage.dto.MPHeadquartersActiveBranches;
import com.dragonwater.backend.Web.User.MyPage.dto.MPHeadquartersDashboardResDto;
import com.dragonwater.backend.Web.User.MyPage.dto.MPOrdersResDto;
import com.dragonwater.backend.Web.User.MyPage.dto.MPUserInformResDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MyPageService {

    /**
     * 유저의 전체적인 정보를 가져오는 메소드
     * @param memberId  member를 식별할 수 있는 id 값
     * @return
     */
    MPUserInformResDto getUserInform(Long memberId);

    /**
     * 유저의 주문 내역을 가져오는 메소드 V1 : 전체를 다 가져오는 것
     * @param memberId  member를 식별할 수 있는 id 값
     * @return
     */
    List<MPOrdersResDto> getUserOrders(Long memberId);

    /**
     * 유저의 주문 내역을 가져오는 메소드 V2 : 페이지네이션 해서 가져오기
     * @param memberId  member를 식별할 수 있는 id 값
     * @param pageable  페이지네이션 정보
     * @return
     */
    List<MPOrdersResDto> getUserOrdersV2(Long memberId, Pageable pageable);

    /**
     * 본사 회원의 대시보드 정보를 가져오는 메소드
     * @param memberId member를 식별할 수 있는 id 값
     * @return
     */
    MPHeadquartersDashboardResDto getHeadquartersDashboardInform(Long memberId);

    /**
     * 회원 정보를 수정하는 메소드
     * @param id member를 식별할 수 있는 id 값
     * @param dto 수정할 정보가 담겨 있는 값
     * @return
     */
    Members updateUser(Long id, MembersUpdateReqDto dto);

    MPHeadquartersActiveBranches getBranchMembers(Long id);
}
