package com.dragonwater.backend.Config.Common.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.Map;

@Service
@Slf4j
public class HttpService {
    public void showHttpINFORM(HttpServletRequest request) {
        log.info("--- 요청 정보 분석 시작 ---");

        // 1. 요청 기본 정보
        log.info("Request Method: {}", request.getMethod()); // 요청 방식 (GET, POST 등)
        log.info("Request URI: {}", request.getRequestURI()); // 요청 경로 (/request-info)
        log.info("Request URL: {}", request.getRequestURL()); // 전체 URL
        log.info("Query String: {}", request.getQueryString()); // 쿼리 파라미터 (?key=value)
        log.info("Protocol: {}", request.getProtocol()); // HTTP/1.1 등

        // 2. 헤더(Header) 정보
        log.info("--- Headers ---");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.info("{}: {}", headerName, request.getHeader(headerName));
        }
        // 특정 헤더만 확인
        log.info("User-Agent Header: {}", request.getHeader("User-Agent"));
        log.info("Authorization Header: {}", request.getHeader("Authorization"));


        // 3. 클라이언트 정보
        log.info("--- Client Info ---");
        log.info("Client IP Address: {}", request.getRemoteAddr()); // 클라이언트 IP
        log.info("Client Host: {}", request.getRemoteHost());
        log.info("Client Port: {}", request.getRemotePort());


        // 4. 요청 파라미터 정보 (주로 application/x-www-form-urlencoded 나 쿼리 스트링)
        log.info("--- Request Parameters ---");
        Map<String, String[]> paramMap = request.getParameterMap();
        paramMap.forEach((key, values) -> {
            log.info("{}: {}", key, String.join(", ", values));
        });


        log.info("--- 요청 정보 분석 종료 ---");
    }
}
