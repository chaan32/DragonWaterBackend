package com.dragonwater.backend.Web.Support.Inquiry.Specific.dto;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

// 관리자 패널에서 볼 문의 내역들
@Data
@Builder
public class AdminSIQnAResDto {
    // 문의 ID
    private Long inquiresId;
    // 문의 대상 제품에 대한 정보
    private Long productId;
    private String productName;

    // 문의자 정보
    private String userName;
    private String userEmail;
    private LocalDateTime createdAt;

    private Boolean isAnswered;

    // 문의 내용 - 답변을 한 경우에는 들어가고 답변 안 했으면 안들어감
    private String question;
    private String answer;

    public static AdminSIQnAResDto of(ProductsInquiries inquiry) {
        Members member = inquiry.getMember();
        Products product = inquiry.getProduct();
        return AdminSIQnAResDto.builder()
                .inquiresId(inquiry.getId())
                .productId(product.getId())
                .productName(product.getName())
                .userName(member.getName())
                .userEmail(member.getEmail())
                .createdAt(inquiry.getCreatedAt())
                .isAnswered(inquiry.getAnswered())
                .question(inquiry.getQuestion())
                .answer(inquiry.getAnswer())
                .build();
    }
}
