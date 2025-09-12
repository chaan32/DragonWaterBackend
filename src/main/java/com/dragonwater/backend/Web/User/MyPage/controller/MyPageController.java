package com.dragonwater.backend.Web.User.MyPage.controller;

import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Web.User.MyPage.dto.MPHeadquartersActiveBranches;
import com.dragonwater.backend.Web.User.MyPage.dto.MPHeadquartersDashboardResDto;
import com.dragonwater.backend.Web.User.MyPage.dto.MPOrdersResDto;
import com.dragonwater.backend.Web.User.MyPage.dto.MPUserInformResDto;
import com.dragonwater.backend.Web.User.Member.dto.update.MembersUpdateReqDto;
import com.dragonwater.backend.Web.User.MyPage.service.MyPageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RequestMapping("/api/users")
@RestController
@Slf4j
@RequiredArgsConstructor
public class MyPageController {
    // 인터페이스 구현 완
    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getUsersInform(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        MPUserInformResDto userInform = myPageService.getUserInform(memberId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("data", userInform);
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정
    @PutMapping("/me")
    public ResponseEntity<?> updateUser(@RequestBody MembersUpdateReqDto membersUpdateReqDto, HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        myPageService.updateUser(memberId, membersUpdateReqDto);

        // 성공 메시지를 담은 Map 객체 생성
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 정보가 성공적으로 수정되었습니다.");

        // .build() 대신 Map 객체를 body에 담아서 반환
        return ResponseEntity.ok(response);
    }


    // 주문 정보 조회 (지점 & 개인의 경우)
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(HttpServletRequest request,
                                       @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        List<MPOrdersResDto> userOrders = myPageService.getUserOrdersV2(memberId, pageable);
        int totalElements = myPageService.getUserOrders(memberId).size();
        HashMap<String, Object> response = new HashMap<>();
        response.put("data", userOrders);
        response.put("totalElements", totalElements);



        return ResponseEntity.ok(response);
    }

    // 본사 관리 대시 보드
    @GetMapping("/orders/headquarters")
    @PreAuthorize("hasRole('HEADQUARTERS')")
    public ResponseEntity<?> getDashBoardHeadQuarters(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        MPHeadquartersDashboardResDto data = myPageService.getHeadquartersDashboardInform(memberId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/headquarters/active/branches")
    @PreAuthorize("hasRole('HEADQUARTERS')")
    public ResponseEntity<?> getActiveBranches(HttpServletRequest request) {
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        MPHeadquartersActiveBranches branchMembers = myPageService.getBranchMembers(memberId);
        HashMap<String, Object> response = new HashMap<>();
        response.put("data", branchMembers);
        return ResponseEntity.ok(response);
    }
}
