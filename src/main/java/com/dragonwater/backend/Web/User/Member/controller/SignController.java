package com.dragonwater.backend.Web.User.Member.controller;


import com.dragonwater.backend.Web.Notify.service.NotifyService;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.dto.register.BranchMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.register.HeadQuarterMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.register.IndividualMbRegReqDto;
import com.dragonwater.backend.Web.User.Member.dto.search.HQInformResDto;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignController {
    private final MemberService memberService;
    private final NotifyService notifyService;

    private final ObjectMapper objectMapper;

    @GetMapping("/check-id/{id}")
    public ResponseEntity<?> checkDuplicateLogInId(@PathVariable String id) {
        Map<String, Boolean> response = new HashMap<>();
        Boolean result = memberService.availableCheckLoginId(id);
        log.info("result :{}", result);
        response.put("available", result);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/register/individual")
    public ResponseEntity<?> registerIndividualMember(@RequestBody IndividualMbRegReqDto dto) {
        Members members = memberService.registerIndividualMember(dto);
        notifyService.notifySuccessRegistration(members);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping(value = "/register/headquarters", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerHeadQuartersMember(@RequestPart("data") String requestDataJson,
                                                        @RequestPart("businessRegistration") MultipartFile businessRegistration) throws JsonProcessingException {

        HeadQuarterMbRegReqDto dto = objectMapper.readValue(requestDataJson, HeadQuarterMbRegReqDto.class);
        log.info("본사 법인 회원 가입 요청 : {}",dto.toString());
        memberService.registerHeadQuartersMember(dto, businessRegistration);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PostMapping(value = "/register/franchise",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> registerBranchMember(@RequestPart("data") String requestDataJson,
                                                  @RequestPart(value = "businessRegistration", required = false) MultipartFile businessRegistration) throws JsonProcessingException {

        BranchMbRegReqDto dto = objectMapper.readValue(requestDataJson, BranchMbRegReqDto.class);
        log.info("법인 지점 회원 가입 요청 : {}", dto.toString());
        if (businessRegistration == null) {
            memberService.registerBranchMember(dto);
        }else {
            memberService.registerBranchMember(dto, businessRegistration);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @GetMapping("/search/headquarters")
    public ResponseEntity<List<HQInformResDto>> searchHeadQuarters(@RequestParam String term) {
        log.info("본사 검색 요청 : {}", term);
        List<HQInformResDto> hqList = memberService.getHQList(term);
        return ResponseEntity.ok(hqList);
    }

    // 비밀번호 초기화 (아이디 체크)
    @PostMapping("/reset/password")
    public ResponseEntity<?> resetPw(@RequestParam String loginId) {
        return null;
    }


}
