package com.dragonwater.backend.Web.User.Member.repository;

import com.dragonwater.backend.Web.User.Member.domain.ApprovalStatus;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HQMemberRepository extends JpaRepository<HeadQuarterMembers, Long> {
    List<HeadQuarterMembers> findByNameContaining (String term);
    List<HeadQuarterMembers> findByApprovalStatus (ApprovalStatus status);
    List<HeadQuarterMembers> findByNameContainingAndApprovalStatus(String term, ApprovalStatus status);
}
