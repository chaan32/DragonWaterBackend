package com.dragonwater.backend.Web.User.Auth.controller;

import com.dragonwater.backend.Config.Common.dto.ApiResponse;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC.DuplicateEmailException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC.DuplicatePhoneException;
import com.dragonwater.backend.Config.Jwt.JwtTokenProvider;
import com.dragonwater.backend.Config.Redis.RedisDao;
import com.dragonwater.backend.Web.Certification.service.CertificationService;
import com.dragonwater.backend.Web.User.Auth.dto.Approve.RequestPhoneAuthDto;
import com.dragonwater.backend.Web.User.Auth.dto.Approve.RequestVerifyPhoneDto;
import com.dragonwater.backend.Web.User.Auth.dto.Login.LogoutRequestDto;
import com.dragonwater.backend.Web.User.Auth.dto.RefreshToken.CreateAccessTokenRequest;
import com.dragonwater.backend.Web.User.Auth.dto.RefreshToken.CreateNewAccessAndRefreshTokenResponse;
import com.dragonwater.backend.Web.User.Auth.dto.Approve.RequestEmailAuthDto;
import com.dragonwater.backend.Web.User.Auth.dto.Approve.RequestVerifyEmailDto;
import com.dragonwater.backend.Web.User.Auth.dto.Login.LoginSuccessTokensDto;
import com.dragonwater.backend.Web.User.Auth.dto.Login.LoginRequestDto;
import com.dragonwater.backend.Web.User.Auth.dto.Login.UpdatePwReqDto;
import com.dragonwater.backend.Web.User.Auth.dto.Reset.FindLoginInformReqDto;
import com.dragonwater.backend.Web.User.Auth.service.AuthService;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping( "/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final MemberService memberService;
    private final CertificationService certificationService;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisDao redisDao;

    /**  로그인 컨트롤러
     *
     * @param loginDto LoginId, LogInPw
     * @return 로그인 성공 시 (200 OK), 로그인 실패 시 (401 Unauthorized)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginDto) {


        LoginSuccessTokensDto data = authService.loginProcess(loginDto);

        ConcurrentHashMap<String, Object> response = new ConcurrentHashMap<>();
        response.put("success", true);
        response.put("message", "로그인 성공");
        response.put("data", data);
        log.info("response : {} ", response);
        return ResponseEntity.status(HttpStatus.OK).body(response) ;
    }

    @PostMapping("/login/recheck")
    public ResponseEntity<?> loginRe(@RequestBody LoginRequestDto loginDto, HttpServletRequest request) {
        log.info("password : {}", loginDto.getPassword());
        Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
        boolean result = authService.passwordReCheck(loginDto, memberId);

        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호 오류");
        }
    }
    @PutMapping("/change/password")
    public ResponseEntity<?> changePW(@RequestBody UpdatePwReqDto pwReqDto, HttpServletRequest request) {
        try {
            Long memberId = jwtTokenProvider.getMemberId(request.getHeader("Authorization").substring(7));
            boolean result = authService.updatePw(pwReqDto, memberId);
            if (result) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("서버 오류 : 비밀번호 변경 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("서버 오류 : 비밀번호 변경 실패");
        }
    }

    @PostMapping("/send/authentication/mail")
    public ResponseEntity<?> sendCodeToEmailAddress(@RequestBody RequestEmailAuthDto dto) {
        log.info("email : {}", dto.getEmail());
        String email = dto.getEmail();
        boolean result = memberService.canUseEmail(email);
        if (!result) {
            throw new DuplicateEmailException(email+"은 중복된 이메일입니다.");
        }
        String authCode = this.generateVerifyMailString();
        certificationService.sendVerificationEmail(email, authCode);
        redisDao.setValues(email, authCode, Duration.ofMinutes(5));
        return ResponseEntity.status(HttpStatus.OK).body("이메일이 발송되었습니다. 인증번호를 입력해주세요 (유효기간 5분)");
    }

    @PostMapping("/send/authentication/phone")
    public ResponseEntity<?> sendCodeToPhoneNumber(@RequestBody RequestPhoneAuthDto dto) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {


        String phone = dto.getPhone();
        log.info("phone : {}", phone);
        boolean result = memberService.canUserPhoneNumber(phone);
        if (!result) {
            throw new DuplicatePhoneException(phone + "은 중복된 번호입니다.");
        }

        String verifyCode = this.generateVerifyPhoneString();
        log.info("저장 send : verifyCode {}", verifyCode);
        redisDao.setValues(phone,verifyCode, Duration.ofMinutes(3));
        certificationService.sendVerificationPhone(dto.getPhone(), verifyCode);
        return ResponseEntity.status(HttpStatus.OK).body("입력하신 번호로 인증번호가 발송되었습니다. 인증번호를 입력해주세요 (유효기간 3분)");
    }

    @PostMapping("/try/mail")
    public ResponseEntity<?>  verifyEmailAddress(@RequestBody RequestVerifyEmailDto dto) {
        String email = dto.getEmail();
        String verifyCode = dto.getVerifyCode();

        if (redisDao.hasValue(email)) { // 있는 경우에
            String values = (String) redisDao.getValues(email);
            if (values.equals(verifyCode)) {
                redisDao.deleteValues(email);
                return ResponseEntity.status(HttpStatus.OK).body("인증이 완료 되었습니다.");
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 실패하였습니다.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효 시간이 만료 되었습니다.");
    }
    @PostMapping("/try/phone")
    public ResponseEntity<?>  verifyPhoneNumber(@RequestBody RequestVerifyPhoneDto dto) {
        String phone= dto.getPhone();
        String verifyCode = dto.getVerifyCode();
        log.info("phone : {}", phone);
        log.info("verify : {}", verifyCode);

        if (redisDao.hasValue(phone)) { // 있는 경우에
            String values = (String) redisDao.getValues(phone);
            log.info("저장 find : verifyCode {}", values);
            if (values.equals(verifyCode)) {
                redisDao.deleteValues(phone);
                return ResponseEntity.status(HttpStatus.OK).body("인증이 완료 되었습니다.");
            }
            else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증이 실패하였습니다.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효 시간이 만료 되었습니다.");
    }




    @PostMapping("/token")
    public ResponseEntity<ApiResponse<CreateNewAccessAndRefreshTokenResponse>> createNewAccessToken(
            @RequestBody CreateAccessTokenRequest request) {


        // jwtTokenProvider를 사용해 새로운 액세스 토큰 생성
        ConcurrentHashMap<String, String> newTokens = jwtTokenProvider.reIssuedAccessToken(request.getRefreshToken());

        // 응답 DTO 생성
        CreateNewAccessAndRefreshTokenResponse response = CreateNewAccessAndRefreshTokenResponse.of(newTokens);

        // 성공 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Access Token, Refresh Token 재발급 성공"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequestDto dto) {
        authService.logout(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find/loginId/cert")
    public ResponseEntity<?> findIdStep01(@RequestBody FindLoginInformReqDto dto) {

        try {
            // 여기서는 이메일 혹은 핸드폰으로 인증번호 발송 해줘야 함


            if (dto.getMethod().equals("email")) { // 이메일로 인증을 시도 했으면,

                String verifyEmailString = generateVerifyPhoneString();
                // ㄹㅔ디스에 인증번호 저장
                redisDao.setValues(dto.getValue(), verifyEmailString);
                certificationService.sendLoginVerifyEmail(dto.getValue(), verifyEmailString);

                return ResponseEntity.ok("인증 번호를 발송했습니다.");
            }

            else if (dto.getMethod().equals("phone")) { // 핸드폰으로 인증을 시도 했으면,

                String verifyPhoneString = generateVerifyPhoneString();
                // ㄹㅔ디스에 인증번호 저장
                redisDao.setValues(dto.getValue(),verifyPhoneString, Duration.ofMinutes(2));
                certificationService.sendLoginVerifyPhone(dto.getValue(), verifyPhoneString);

                return ResponseEntity.ok("인증 번호를 발송했습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Error | UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException |
                 JsonProcessingException error) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/find/loginId/send")
    public ResponseEntity<?> findIdByPhone(@RequestBody FindLoginInformReqDto dto) {
        String verifyCodeInRedis = (String) redisDao.getValues(dto.getValue());
        redisDao.deleteValues(dto.getValue());
        if (verifyCodeInRedis.equals(dto.getVerifyCode())) { // 인증 성공

            Members member = dto.getMethod().equals("email")
                    ? memberService.getMemberByEmail(dto.getValue())
                    : memberService.getMemberByPhone(dto.getValue());




            // 이메일 발송
            certificationService.sendLoginId(member);

            ConcurrentHashMap<String, String> response = new ConcurrentHashMap<>();
            response.put("message", maskEmail(member.getEmail())+"로 로그인 아이디를 보냈습니다.");
            return ResponseEntity.ok(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /*
            비밀번호 찾는 로직
            1. 아이디를 인증 함 -> 있음
            2. 본인 인증 -> 핸드폰 혹은 이메일로
            3. 저장된 이메일로 임시 비밀번호 발송

     */
    @PostMapping("/find/password/cert")
    public ResponseEntity<?> findPwStep01(@RequestBody FindLoginInformReqDto dto) {
        Members memberByLoginId = memberService.getMemberByLoginId(dto.getLoginId());
        try {
            // 여기서는 이메일 혹은 핸드폰으로 인증번호 발송 해줘야 함
            if (dto.getMethod().equals("email")
                    && memberByLoginId.getEmail().equals(dto.getValue())) { // 이메일로 인증을 시도 했으면,

                String verifyEmailString = generateVerifyPhoneString();
                // ㄹㅔ디스에 인증번호 저장
                redisDao.setValues(dto.getValue(), verifyEmailString);
                certificationService.sendLoginVerifyEmail(dto.getValue(), verifyEmailString);

                return ResponseEntity.ok("인증 번호를 발송했습니다.");
            } else if (dto.getMethod().equals("phone")
                    && memberByLoginId.getPhone().equals(dto.getValue())) { // 핸드폰으로 인증을 시도 했으면,

                String verifyPhoneString = generateVerifyPhoneString();
                // ㄹㅔ디스에 인증번호 저장
                redisDao.setValues(dto.getValue(),verifyPhoneString, Duration.ofMinutes(2));
                certificationService.sendLoginVerifyPhone(dto.getValue(), verifyPhoneString);

                return ResponseEntity.ok("인증 번호를 발송했습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Error | UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException |
                 JsonProcessingException error) {return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/find/password/reset")
    public ResponseEntity<?> findPwStep02(@RequestBody FindLoginInformReqDto dto) {
        String verifyCodeInRedis = (String) redisDao.getValues(dto.getValue());
        redisDao.deleteValues(dto.getValue());
        if (verifyCodeInRedis.equals(dto.getVerifyCode())) { // 인증 성공

            Members member = dto.getMethod().equals("email")
                    ? memberService.getMemberByEmail(dto.getValue())
                    : memberService.getMemberByPhone(dto.getValue());


            String tempPassword = generateTempPassword();
            Members members = memberService.resetPassword(member, tempPassword);

            // 이메일 발송
            certificationService.sendResetPassword(members, tempPassword);

            ConcurrentHashMap<String, String> response = new ConcurrentHashMap<>();
            response.put("message", maskEmail(members.getEmail())+"로 임시 비밀번호를 발급 보냈습니다.");
            return ResponseEntity.ok(response);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    private String maskEmail(String email) {
        // 이메일 주소 패턴: @ 기호를 기준으로 아이디와 도메인 분리
        Pattern pattern = Pattern.compile("(.{2})(.*)(@.*)");
        Matcher matcher = pattern.matcher(email);

        if (matcher.find()) {
            // matcher.group(1): 아이디의 첫 두 글자
            // matcher.group(2): 아이디의 나머지 부분
            // matcher.group(3): @를 포함한 도메인

            // 아이디의 나머지 부분을 *로 대체
            String maskedId = matcher.group(2).replaceAll(".", "*");

            // 마스킹된 아이디와 원래의 첫 두 글자, 도메인을 결합
            return matcher.group(1) + maskedId + matcher.group(3);
        }
        return email; // 패턴에 맞지 않으면 원래 이메일 반환
    }

    private String generateTempPassword() {
        return RandomStringUtils.randomAlphanumeric(14);
    }


    private String generateVerifyMailString() {
        return RandomStringUtils.randomAlphanumeric(20);
    }
    private String generateVerifyPhoneString(){
        Random random = new Random();
        String verifyCode = "";
        for (int i = 0; i < 5; i++) {
            verifyCode += random.nextInt(10);
        }
        return verifyCode;
    }
}
