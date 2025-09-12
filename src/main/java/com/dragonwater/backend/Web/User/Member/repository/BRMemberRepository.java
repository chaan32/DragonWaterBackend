package com.dragonwater.backend.Web.User.Member.repository;

import com.dragonwater.backend.Web.User.Member.domain.ApprovalStatus;
import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BRMemberRepository extends JpaRepository<BranchMembers, Long> {
    List<BranchMembers> findByApprovalStatus (ApprovalStatus status);
}
