package com.dragonwater.backend.Web.Support.Claim.repository;

import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claims, Long> {
    List<Claims> findByMemberId(Long id);
}
