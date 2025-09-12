package com.dragonwater.backend.Web.Support.Notice.domain;


import com.dragonwater.backend.Web.Support.Notice.dto.AdminNoticeAddReqDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notices {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "title", length = 100)
    private String title;

    @Lob
    private String content;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "views")
    private Long views;

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

    public static Notices of(AdminNoticeAddReqDto dto) {
        return Notices.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .pinned(dto.getPinned())
                .views(0L)
                .build();
    }
    public void addView() {
        this.views++;
    }

    public Notices edit(AdminNoticeAddReqDto dto) {
        this.content = dto.getContent();
        this.pinned = dto.getPinned();
        this.title = dto.getTitle();
        return this;
    }
}
