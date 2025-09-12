package com.dragonwater.backend.Web.Support.Inquiry.General.domain;


import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryReqDto;
import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralInquiries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Members member;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "isAnswered", nullable = false)
    private Boolean isAnswered;

    @Column(name = "answer")
    private String answer;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Orders order;

    @Column(name = "gallery_image_s3_url_0", nullable = true)
    private String galleryImagesS3URL0;
    @Column(name = "gallery_image_s3_url_1", nullable = true)
    private String galleryImagesS3URL1;
    @Column(name = "gallery_image_s3_url_2", nullable = true)
    private String galleryImagesS3URL2;
    @Column(name = "gallery_image_s3_url_3", nullable = true)
    private String galleryImagesS3URL3;
    @Column(name = "gallery_image_s3_url_4", nullable = true)
    private String galleryImagesS3URL4;

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

    public static GeneralInquiries of(GeneralInquiryReqDto dto, Members member, Orders orders) {
        return GeneralInquiries.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .isAnswered(false)
                .order(orders)
                .category(dto.getCategory())
                .build();
    }

    public void addImageUrls(List<String> urls) {
        // 기존 필드 초기화
        this.galleryImagesS3URL0 = null;
        this.galleryImagesS3URL1 = null;
        this.galleryImagesS3URL2 = null;
        this.galleryImagesS3URL3 = null;
        this.galleryImagesS3URL4 = null;

        if (urls == null || urls.isEmpty()) {
            return;
        }

        // 최대 5개까지만 할당
        for (int i = 0; i < urls.size() && i < 5; i++) {
            switch (i) {
                case 0:
                    this.galleryImagesS3URL0 = urls.get(i);
                    break;
                case 1:
                    this.galleryImagesS3URL1 = urls.get(i);
                    break;
                case 2:
                    this.galleryImagesS3URL2 = urls.get(i);
                    break;
                case 3:
                    this.galleryImagesS3URL3 = urls.get(i);
                    break;
                case 4:
                    this.galleryImagesS3URL4 = urls.get(i);
                    break;
            }
        }
    }

    public GeneralInquiries enrollAnswer(String answer) {
        this.answer = answer;
        this.isAnswered = true;
        return this;
    }
}
