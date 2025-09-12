package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BranchResDto {
    private Role memberType;
    private String branchName; // 지점명
    private String parentName; // 본사명
    private String bizRegImageUrl;
    private String bizRegNumber;
    private String bizType;
    private String managerName;
    private String managerPhone;

    public static BranchResDto of(BranchMembers branchMembers) {
        return BranchResDto.builder()
                .memberType(branchMembers.getRole())
                .branchName(branchMembers.getBranchName())
                .parentName(branchMembers.getName())
                .bizRegImageUrl(branchMembers.getBizRegUrl())
                .bizRegNumber(branchMembers.getBusinessRegistrationNumber())
                .bizType(branchMembers.getBusinessType())
                .managerName(branchMembers.getManagerName())
                .managerPhone(branchMembers.getManagerPhone())
                .build();
    }
}
