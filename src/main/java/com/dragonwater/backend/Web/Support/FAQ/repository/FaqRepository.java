package com.dragonwater.backend.Web.Support.FAQ.repository;

import com.dragonwater.backend.Web.Support.FAQ.domain.FAQs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<FAQs, Long> {
    List<FAQs> findByCategoryId(Long id);
}
