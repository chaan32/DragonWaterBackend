package com.dragonwater.backend.Web.Support.Notice.repository;

import com.dragonwater.backend.Web.Support.Notice.domain.Notices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notices, Long> {
}
