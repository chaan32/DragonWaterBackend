package com.dragonwater.backend.Web.Support.Claim.domain;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryReqDto;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claims {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 주문 정보 1:1로 맵핑
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Members member;

    // claim 종류와 상세 설명
    @Column(name = "claim_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;
    @Column(name = "claim_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClaimStatus claimStatus;
    @Column(name = "title", length = 50)
    private String title;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "rejected_reason")
    private String rejectedReason;
    // 사진
    @Column(length = 512)
    private String image1Url;
    @Column(length = 512)
    private String image2Url;
    @Column(length = 512)
    private String image3Url;
    @Column(length = 512)
    private String image4Url;
    @Column(length = 512)
    private String image5Url;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public static Claims of(Orders order, GeneralInquiryReqDto dto, Members member) {
        return Claims.builder()
                .order(order)
                .claimStatus(ClaimStatus.RECEIVED)
                .claimType(dto.getCategory().equals("exchange")?ClaimType.EXCHANGE:ClaimType.REFUND)
                .title(dto.getTitle())
                .description(dto.getContent())
                .member(member)
                .build();
    }
    public void addImageUrls(List<String> urls) {
        // 기존 필드 초기화
        this.image1Url = null;
        this.image2Url = null;
        this.image3Url = null;
        this.image4Url = null;
        this.image5Url = null;

        if (urls == null || urls.isEmpty()) {
            return;
        }

        // 최대 5개까지만 할당
        for (int i = 0; i < urls.size() && i < 5; i++) {
            switch (i) {
                case 0:
                    this.image1Url = urls.get(i);
                    break;
                case 1:
                    this.image2Url = urls.get(i);
                    break;
                case 2:
                    this.image3Url = urls.get(i);
                    break;
                case 3:
                    this.image4Url = urls.get(i);
                    break;
                case 4:
                    this.image5Url = urls.get(i);
                    break;
            }
        }
    }

    public void reject(String reason) {
        this.claimStatus = ClaimStatus.REJECTED;
        this.rejectedReason = reason;
    }
    public void approve() {
        this.claimStatus = ClaimStatus.COMPLETED;
    }
}
