package com.dragonwater.backend.Web.Support.Comment.service;

import com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC.DuplicateCommentException;
import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Shop.Product.dto.product.ReviewReqDto;
import com.dragonwater.backend.Web.Shop.Product.service.interf.ProductService;
import com.dragonwater.backend.Web.Support.Comment.domain.Comments;
import com.dragonwater.backend.Web.Support.Comment.dto.CommentsResDto;
import com.dragonwater.backend.Web.Support.Comment.repository.CommentRepository;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    // 인터페이스 구현 완
    private final MemberService memberService;
    private final ProductService productService;
    // 인터페이스 구현 미완


    private final CommentRepository commentRepository;


    @Override
    public Queue<CommentsResDto> getProductComments(Long productId) {
        Queue<CommentsResDto> dtos = new LinkedList<>();
        List<Comments> commentsByProductId = commentRepository.findCommentsByProductId(productId);
        for (Comments comments : commentsByProductId) {
            dtos.add(CommentsResDto.of(comments));
        }
        return dtos;
    }

    @Override
    @Transactional
    public void addComment(ReviewReqDto dto,Long memberId) {
        if (checkDidComment(dto, memberId)) {
            throw new DuplicateCommentException();
        }
        Products product = productService.getProductById(dto.getProductId());
        Members member = memberService.getMemberById(memberId);
        Comments comments = Comments.of(dto, product, member);
        product.calculateRating(comments);
        product.addComment(comments);
        commentRepository.save(comments);
    }

    @Override
    public boolean checkDidComment(ReviewReqDto dto, Long memberId) {
        String orderNumber = dto.getOrderName();
        Long productId = dto.getProductId();

        if (commentRepository.findByMemberIdAndProductIdAndOrderNumber(memberId, productId, orderNumber).isEmpty()){
            return false;
        }
        return true;
    }
}
