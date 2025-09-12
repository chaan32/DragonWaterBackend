package com.dragonwater.backend.Config.Security;

import com.dragonwater.backend.Web.Shop.Product.domain.ProductDisplay;
import com.dragonwater.backend.Web.Shop.Product.repository.DisplayRepository;
import com.dragonwater.backend.Web.User.Member.domain.AdminMembers;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import com.dragonwater.backend.Web.User.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Member;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final DisplayRepository displayRepository;
    private final PasswordEncoder encoder;
    private final String adminId = "admin";
    private final String adminPw = "admin";
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!memberRepository.existsByRole(Role.ADMIN)){
            log.info("관리자 계정 생성");
            AdminMembers admin = AdminMembers.of(adminId, encoder.encode(adminPw));
            memberRepository.save(admin);
        }


        if (displayRepository.findFirstByOrderByIdAsc().isEmpty()) {
            log.info("Display 객체 생성");
            ProductDisplay productDisplay = ProductDisplay.of(null, null, null);
            displayRepository.save(productDisplay);
        }

    }
}
