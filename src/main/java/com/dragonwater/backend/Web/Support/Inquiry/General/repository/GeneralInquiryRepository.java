package com.dragonwater.backend.Web.Support.Inquiry.General.repository;

import com.dragonwater.backend.Web.Support.Inquiry.General.domain.GeneralInquiries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralInquiryRepository extends JpaRepository<GeneralInquiries, Long> {
    List<GeneralInquiries> findByIsAnswered (Boolean isAnswered);

    List<GeneralInquiries> findByMemberId(Long id);
}
