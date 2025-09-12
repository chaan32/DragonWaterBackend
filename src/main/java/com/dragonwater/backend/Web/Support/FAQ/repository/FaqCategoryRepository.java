package com.dragonwater.backend.Web.Support.FAQ.repository;

import com.dragonwater.backend.Web.Support.FAQ.domain.FaqCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqCategoryRepository extends JpaRepository<FaqCategories, Long> {
}
