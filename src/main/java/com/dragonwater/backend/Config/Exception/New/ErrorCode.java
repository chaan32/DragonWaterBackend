package com.dragonwater.backend.Config.Exception.New;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "COMMON_ERROR_001", "서버 내부 오류가 발생했습니다."),
    INVALID_REQUEST(400, "COMMON_ERROR_002", "잘못된 요청입니다."),

    // 인증/인가
    LOGIN_FAILED(401, "AUTH_ERROR_001", "아이디/비밀번호가 일치하지 않습니다."),
    APPROVAL_PENDING(403, "AUTH_ERROR_002", "법인 승인 검토 중입니다."),
    ACCESS_DENIED(403, "AUTH_ERROR_003", "접근 권한이 없습니다."),
    EXPIRED_ACCESS_TOKEN(401, "AUTH_ERROR_004", "토큰의 유효기간이 만료되었습니다."),
    AUTHENTICATION_REQUIRED(401, "AUTH_ERROR_005", "로그인이 필요합니다."),

    // 회원
    MEMBER_NOT_FOUND(404, "MEMBER_ERROR_001", "회원을 찾을 수 없습니다."),
    DUPLICATE_LOGIN_ID(409, "MEMBER_ERROR_002", "이미 사용 중인 아이디입니다."),
    DUPLICATE_EMAIL(409, "MEMBER_ERROR_004", "이미 사용 중인 이메일입니다."),
    DUPLICATE_PHONE(409, "MEMBER_ERROR_005", "이미 사용 중인 핸드폰 번호입니다."),
    SIGNUP_FAILED(400, "MEMBER_ERROR_003", "회원가입에 실패했습니다."),

    // 상품
    PRODUCT_NOT_FOUND(404, "PRODUCT_ERROR_001", "상품을 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(404, "PRODUCT_ERROR_002", "카테고리를 찾을 수 없습니다."),

    // 주문
    ORDER_NOT_FOUND(404, "ORDER_ERROR_001", "주문을 찾을 수 없습니다."),
    INVALID_ORDER_STATUS(400, "ORDER_ERROR_002", "유효하지 않은 주문 상태입니다."),
    ORDER_FAIL_STOCK(400, "ORDER_ERROR_003", "재고가 품절 되었습니다."),

    // 공지사항
    NOTICE_NOT_FOUND(404, "NOTICE_ERROR_001", "공지사항을 찾을 수 없습니다."),
    NOTICE_UPLOAD_FAILED(400, "NOTICE_ERROR_002", "공지사항 작성에 실패했습니다."),

    // 클레임
    CLAIM_NOT_FOUND(404, "CLAIM_ERROR_001", "클레임을 찾을 수 없습니다."),
    CLAIM_FAILED(400, "CLAIM_ERROR_002", "클레임 처리에 실패했습니다."),
    DUPLICATE_CLAIM(409, "CLAIM_ERROR_002", "주문 하나 당 클레임은 1번 작성할 수 있습니다."),

    // FAQ 카테고리
    FAQ_CATEGORY_NOT_FOUND(404, "FAQCATEG_ERROR_001", "FAQ CATEGORY를 찾을 수 없습니다."),

    // 댓글
    COMMENT_FAILED(400, "COMMENT_ERROR_001", "댓글 작성에 실패했습니다."),
    DUPLICATE_COMMENT(409, "COMMENT_ERROR_002", "이미 작성한 댓글입니다."),


    // FAQ
    FAQ_NOT_FOUND(404, "FAQ_ERROR_001", "FAQ를 찾을 수 없습니다."),

    // 문의
    INQUIRY_NOT_FOUND(404, "INQUIRY_ERROR_001", "문의를 찾을 수 없습니다."),

    // 이미지
    IMAGE_NOT_FOUND(404, "IMAGE_ERROR_001", "이미지를 찾을 수 없습니다."),

    // 수정
    EDIT_FAILED(400, "EDIT_ERROR_001", "수정에 실패했습니다."),

    // 삭제
    DELETE_FAILED(400, "DELETE_ERROR_001", "삭제에 실패했습니다."),


    // 추가
    ADD_FAILED(400, "ADD_ERROR_001", "추가에 실패했습니다."),

    //실패
    FAILED_SEND_EMAIL(500, "FAILED_ERROR_001", "이메일 발송에 실패했습니다."),
    FAILED_SEND_SMS(500, "FAILED_ERROR_002", "문자 발송에 실패했습니다."),
    FAILED_UPLOAD_IMAGE(500, "FAILED_ERROR_003", "이미지 업로드에 실패했습니다.");

    private final int httpStatus;
    private final String code;
    private final String message;

    ErrorCode(int httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public int getHttpStatus() { return httpStatus; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
}
