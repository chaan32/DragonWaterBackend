package com.dragonwater.backend.Web.Support.FAQ.domain;


import com.dragonwater.backend.Web.Support.FAQ.dto.FaqReqDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FAQs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_id", nullable = false)
    @ToString.Exclude
    private FaqCategories category;

    @Column(name = "question", nullable = false, length = 50)
    private String question;

    @Column(name = "answer", nullable = false, length = 40)
    private String answer;

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

    public static FAQs of(FaqCategories category, FaqReqDto dto) {
        return FAQs.builder()
                .category(category)
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .build();
    }

    public FAQs edit(FaqReqDto dto) {
        this.question = dto.getQuestion();
        this.answer = dto.getAnswer();
        return this;
    }
}
