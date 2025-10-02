package com.dragonwater.backend.Web.KakaoNotify.service;

import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoNotiService {
    private static final Integer TIME_OUT = 5000;

    // 연동 개발 인증 키
    @Value("${ppurio.api.key}")
    private String API_KEY;

    // 뿌리오 계정
    @Value("${ppurio.account}")
    private String PPURIO_ACCOUNT;

    // 발신프로필명
    @Value("${ppurio.sender-profile}")
    private String SENDER_PROFILE;

    // 템플릿 코드 (테스트)
    @Value("${ppurio.template-code.to-admin}")
    private String TEMPLATE_CODE;

    @Value("${ppurio.template-code.to-customer}")
    private String TEMPLATE_CODE_TO_CUSTOMER;
    @Value("${ppurio.template-code.to-admin}")
    private String TEMPLATE_CODE_TO_ADMIN;


    private static final String URI = "https://message.ppurio.com";

    // 발송을 요청하는
    @Async
    public void requestSend(HashMap<String, String> vars, Boolean flag) {
        log.info("request Send 1)");
//        String basicAuthorization = Base64.getEncoder().encodeToString((PPURIO_ACCOUNT + ":" + API_KEY).getBytes());
        String basicAuthorization = getBasicAuth();
        log.info("request Send 2)");
        Map<String, Object> tokenResponse = getToken(URI, basicAuthorization); // 토큰 발급
        log.info("token : {}",(String) tokenResponse.get("token"));
        log.info("request Send 3)");
        Map<String ,Object> sendResponse = send(URI, (String) tokenResponse.get("token"), vars, flag); // 발송 요청
        log.info("request Send 4)");
        System.out.println(sendResponse.toString());
    }

    public void requestCancel() {
        String basicAuthorization = Base64.getEncoder().encodeToString((PPURIO_ACCOUNT + ":" + API_KEY).getBytes());

        Map<String, Object> tokenResponse = getToken(URI, basicAuthorization); // 토큰 발급
        Map<String, Object> cancelResponse = cancel(URI, (String) tokenResponse.get("token")); // 예약 취소 요청

        System.out.println(cancelResponse.toString());
    }

    /**
     * Access Token 발급 요청 (한 번 발급된 토큰은 24시간 유효합니다.)
     * @param baseUri 요청 URI ex) https://message.ppurio.com
     * @param BasicAuthorization "계정:연동 개발 인증키"를 Base64 인코딩한 문자열
     * @return Map
     */
    private Map<String, Object> getToken(String baseUri, String BasicAuthorization) {
        HttpURLConnection conn = null;
        try {
            // 요청 파라미터 생성
            Request request = new Request(baseUri + "/v1/token", "Basic " + BasicAuthorization);

            // 요청 객체 생성
            conn = createConnection(request);

            // 응답 데이터 객체 변환
            return getResponseBody(conn);
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 카카오(알림톡) 발송 요청
     * @param baseUri 요청 URI ex) https://message.ppurio.com
     * @param accessToken 토큰 발급 API를 통해 발급 받은 Access Token, 유효기간이 1일이기 때문에 만료될 경우 재발급 필요
     * @param vars 템플릿에 넣을 변수들
     * @param flag 관리자에게 보낼건지 (false), 고객에게 보낼건지(true)
     * @return Map
     */
    private Map<String, Object> send(String baseUri, String accessToken, HashMap<String, String> vars, Boolean flag) {

        HttpURLConnection conn = null;
        try {
            // 요청 파라미터 생성
            String bearerAuthorization = String.format("%s %s", "Bearer", accessToken);
            Request request = new Request(baseUri + "/v1/kakao", bearerAuthorization);

            // 요청 객체 생성
            // 알림톡(텍스트) 발송

            conn = flag ? createConnection(request, createSendToCustomerParams(vars))
                        : createConnection(request, createSendToAdminParams(vars));

            // 응답 데이터 객체 변환
            return getResponseBody(conn);
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * 예약발송 취소 요청
     * @param baseUri 요청 URI ex) https://message.ppurio.com
     * @param accessToken 토큰 발급 API를 통해 발급 받은 Access Token, 유효기간이 1일이기 때문에 만료될 경우 재발급 필요
     * @return Map
     */
    private Map<String, Object> cancel(String baseUri, String accessToken) {
        HttpURLConnection conn = null;
        try {
            // 요청 파라미터 생성
            String token = String.format("%s %s", "Bearer", accessToken);
            Request request = new Request(baseUri + "/v1/cancel/kakao", token);

            // 요청 객체 생성
            conn = createConnection(request, createCancelTestParams());// 예약 취소 테스트

            // 응답 데이터 객체 변환
            return getResponseBody(conn);
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private <T> HttpURLConnection createConnection(Request request, T requestObject) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(requestObject);
        // 요청 객체 생성
        HttpURLConnection connect = createConnection(request);
        connect.setDoOutput(true); // URL 연결을 출력용으로 사용(true)
        // 요청 데이터 처리
        try (OutputStream os = connect.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return connect;
    }

    private HttpURLConnection createConnection(Request request) throws IOException {
        URL url = new URL(request.getRequestUri());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", request.getAuthorization()); // Authorization 헤더 입력
        conn.setConnectTimeout(TIME_OUT); // 연결 타임아웃 설정(5초)
        conn.setReadTimeout(TIME_OUT); // 읽기 타임아웃 설정(5초)
        return conn;
    }

    private Map<String, Object> getResponseBody(HttpURLConnection conn) throws IOException {
        InputStream inputStream;

        if (conn.getResponseCode() == 200) { // 요청 성공
            inputStream = conn.getInputStream();
        } else { // 서버에서 요청은 수신했으나 특정 이유로 인해 실패함
            inputStream = conn.getErrorStream();
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String inputLine;
            StringBuilder responseBody = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                responseBody.append(inputLine);
            }

            log.info("PPURIO response raw body: {}", responseBody.toString());

            // 성공 응답 데이터 변환
            return convertJsonToMap(responseBody.toString());
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

    private Map<String, Object> convertJsonToMap(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<>() {});
    }

    private Map<String, Object> createSendTestParams() throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", PPURIO_ACCOUNT);
        params.put("messageType", "ALT");
        params.put("senderProfile", SENDER_PROFILE);
        params.put("templateCode", TEMPLATE_CODE);
        params.put("duplicateFlag", "Y");
        params.put("isResend", "Y");
        params.put("targetCount", 1);
        params.put("targets", List.of(
                Map.of("to", "01012341234",
                        "name", "tester",
                        "changeWord", Map.of(
                                "var1", "ppurio api world")))
        );
        params.put("resend", List.of(
                Map.of("messageType", "SMS",
                        "from", "01012341234",
                        "content", "hello this is sms test",
                        "subject", "sms test"))
        );
        params.put("refKey", RandomStringUtils.random(32, true, true)); // refKey 생성, 32자 이내로 아무 값이든 상관 없음
        return params;
    }

    // 고객에게 주문 완료 메세지를 보내는 것
    private Map<String, Object> createSendToCustomerParams(HashMap<String, String> vars) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", PPURIO_ACCOUNT);
        params.put("messageType", "ALH");
        params.put("senderProfile", SENDER_PROFILE);
        params.put("templateCode", TEMPLATE_CODE_TO_CUSTOMER);
        params.put("duplicateFlag", "N");
        params.put("isResend", "N");
        params.put("targetCount", 1);
        params.put("targets", List.of(
                Map.of("to", vars.get("phone"),
                        "name", vars.get("name"),
                        "changeWord", Map.of(
                                "var1", vars.get("orderNumber") , // 주문번호
                                "var2", vars.get("productsName"), // 주문 상품
                                "var3",vars.get("orderDate"), // 주문 일시
                                "var4",vars.get("orderPrice"), // 결제 금액
                                "var5",vars.get("deliveryAddress") // 배송 주소
                        )))
        );

        params.put("refKey", "dragon-water"); // refKey 생성, 32자 이내로 아무 값이든 상관 없음
        return params;
    }
    private Map<String, Object> createSendToAdminParams(HashMap<String, String> vars) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", PPURIO_ACCOUNT);
        params.put("messageType", "ALH");
        params.put("senderProfile", SENDER_PROFILE);
        params.put("templateCode", TEMPLATE_CODE_TO_ADMIN);
        params.put("duplicateFlag", "N");
        params.put("isResend", "N");
        params.put("targetCount", 1);
        params.put("targets", List.of(
                Map.of("to", "01046587418",
                        "changeWord", Map.of(
                                "var1", vars.get("orderNumber") , // 주문번호
                                "var2", vars.get("productsName"), // 주문 상품
                                "var3",vars.get("orderPrice"), // 결제 금액
                                "var4",vars.get("name"), // 주문 고객
                                "var5",vars.get("deliveryAddress"), // 배송 주소
                                "var6",vars.get("orderDate")
                        )))
        );

        params.put("refKey", "dragon-water"); // refKey 생성, 32자 이내로 아무 값이든 상관 없음
        return params;
    }


    private Map<String, Object> createCancelTestParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account", PPURIO_ACCOUNT);
        params.put("messageKey", "230413110135117SMS029914servsUBn");
        return params;
    }

    private String getBasicAuth(){
        String format = String.format("%s:%s", PPURIO_ACCOUNT, API_KEY);
        log.info("format : {}",format);

        return Base64.getEncoder().encodeToString(format.getBytes());
    }

}

class Request {
    private String requestUri;
    private String authorization;

    public Request(String requestUri, String authorization) {
        this.requestUri = requestUri;
        this.authorization = authorization;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getAuthorization() {
        return authorization;
    }
}
