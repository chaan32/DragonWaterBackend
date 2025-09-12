package com.dragonwater.backend.Web.Support.Inquiry.Specific.repository;

import com.dragonwater.backend.Web.Support.Inquiry.Specific.domain.ProductsInquiries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecificInquiryRepository extends JpaRepository<ProductsInquiries, Long> {
    List<ProductsInquiries> findByProductIdAndAnswered(Long productId, Boolean answered);

    List<ProductsInquiries> findByAnswered(Boolean answered);
    List<ProductsInquiries> findByProductId(Long productId);
    List<ProductsInquiries> findByMemberId(Long memberId);

}
