package com.dragonwater.backend.Web.Support.Comment.service;

import com.dragonwater.backend.Web.Shop.Product.dto.product.ReviewReqDto;
import com.dragonwater.backend.Web.Support.Comment.dto.CommentsResDto;

import java.util.Queue;

public interface CommentService {

    /**
     * 상품의 코멘트를 가져올 메소드
     * @param productId 상품를 식별할 수 있는 id 값
     * @return
     */
    Queue<CommentsResDto> getProductComments(Long productId);


    /**
     * 코멘트를 추가하는 메소드
     * @param dto 댓글 내용에 대한 DTO
     * @param memberId 작성자를 식별할 수 있는 id 값
     */
    void addComment(ReviewReqDto dto, Long memberId);

    /**
     * 코멘트는 1회성인데 달았는지 여부를 체크하는 메소드
     * @param dto 코멘트 정보를 담은 DTO
     * @param memberId 작성자를 식별할 수 있는 id 값
     * @return
     */
    boolean checkDidComment(ReviewReqDto dto, Long memberId);
}
