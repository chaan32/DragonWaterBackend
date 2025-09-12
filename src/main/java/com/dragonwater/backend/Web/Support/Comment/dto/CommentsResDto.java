package com.dragonwater.backend.Web.Support.Comment.dto;

import com.dragonwater.backend.Web.Support.Comment.domain.Comments;
import lombok.Builder;
import lombok.Data;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentsResDto {
    private Long commentId;
    private Long userId;
    private String comment;
    private LocalDateTime createdAt;
    private String authorName;
    private Integer rating;

    public static CommentsResDto of(Comments comment) {
        String outputName = comment.getMember().getName().substring(0,1)+"**";

        return CommentsResDto.builder()
                .commentId(comment.getId())
                .userId(comment.getMember().getId())
                .comment(comment.getContent())
                .authorName(outputName)
                .createdAt(comment.getCreatedAt())
                .rating(comment.getRating())
                .build();
    }
}
