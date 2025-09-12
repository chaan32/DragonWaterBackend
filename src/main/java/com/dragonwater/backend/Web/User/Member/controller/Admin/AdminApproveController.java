package com.dragonwater.backend.Web.User.Member.controller.Admin;

import com.dragonwater.backend.Web.Notify.service.NotifyService;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.dto.corporate.ApproveResponseDto;
import com.dragonwater.backend.Web.User.Member.dto.order.OrderMinimalResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.CorporatesResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.IndividualsResDto;
import com.dragonwater.backend.Web.User.Member.dto.search.MemberInformDto;
import com.dragonwater.backend.Web.User.Member.service.AdminService;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminApproveController {
    // 인터페이스 구현 완료
    private final AdminService adminService;
    private final MemberService memberService;
    private final NotifyService notifyService;

    // 승인 대기 중인 업체 리턴
    @GetMapping("/corporate-requests")
    public ResponseEntity<?> getCorporateRequestList() {
        log.info("corporate Request List");
        List<ApproveResponseDto> pendingStateCorporations = adminService.getPendingStateCorporations();
        return ResponseEntity.ok(pendingStateCorporations);
    }

    // 법인 회원 승인
    @PostMapping("/corporate-requests/{id}/approve")
    public ResponseEntity<?> approveCorporate(@PathVariable String id) {
        log.info("corporate Request - approve id - {}", id);
        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        Boolean result = adminService.approveCorporates(Long.parseLong(id));
        Members member = memberService.getMemberById(Long.valueOf(id));
        if (result) {
            response.put("success", result);
            response.put("message", "승인이 완료 되었습니다.");
            notifyService.notifySuccessRegistration(member);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 법인 회원 거절
    @PostMapping("/corporate-requests/{id}/reject")
    public ResponseEntity<?> rejectCorporate(@PathVariable String id) {
        log.info("corporate Request - reject id - {}", id);

        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        Boolean result = adminService.rejectCorporates(Long.parseLong(id));

        if (result) {
            response.put("success", result);
            response.put("message", "거절이 완료 되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 개인 회원 가져오기
    @GetMapping("/members/individual")
    public ResponseEntity<?> membersListIndividual() {
        log.info("이거 쓰냐??? 44444");
        log.info("individual members list ");
        List<IndividualsResDto> list = adminService.getIndividualMembersList();

        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("data", list);
        return ResponseEntity.ok(response);
    }

    // 법인 회원 가져오기
    @GetMapping("/members/corporate")
    public ResponseEntity<?> membersListCorporate() {
        log.info("이거 쓰냐??? 55555");
        log.info("corporate members list ");
        List<CorporatesResDto> list = adminService.getCorporateMembersList();


        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("data", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members")
    public ResponseEntity<?> memberListAll() {
        log.info("이거 쓰냐??? 777777");
        List<MemberInformDto> allMembersList = adminService.getAllMembersList();
        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("data", allMembersList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/{memberId}/orders")
    public ResponseEntity<?> getMemberOrderHistory(@PathVariable Long memberId) {
        List<OrderMinimalResDto> orders = adminService.getOrders(memberId);
        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("data", orders);
        return ResponseEntity.ok(response);
    }


}
