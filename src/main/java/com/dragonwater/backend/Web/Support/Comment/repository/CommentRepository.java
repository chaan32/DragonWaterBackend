package com.dragonwater.backend.Web.Support.Comment.repository;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.Support.Comment.domain.Comments;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findCommentsByProductId (Long productId);

    Optional<Comments> findByMemberIdAndProductIdAndOrderNumber(Long memberId, Long productId, String orderNumber);
}
