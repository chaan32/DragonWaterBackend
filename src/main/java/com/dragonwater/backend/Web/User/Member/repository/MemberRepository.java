package com.dragonwater.backend.Web.User.Member.repository;

import com.dragonwater.backend.Web.User.Member.domain.ApprovalStatus;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {
    Optional<Members> findByLoginId(String loginId);

    Optional<Members> findById(Long Id);

    Boolean existsByRole(Role role);

    Long id(Long id);

    List<Members> findByRole(Role role);

    Optional<Members> findByEmail(String email);
    Optional<Members> findByPhone(String phone);


}
