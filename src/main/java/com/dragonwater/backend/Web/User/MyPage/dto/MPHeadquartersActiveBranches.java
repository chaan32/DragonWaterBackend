package com.dragonwater.backend.Web.User.MyPage.dto;

import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import lombok.Builder;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
public class MPHeadquartersActiveBranches {
    private List<BranchMemberInfo> branches;

    public static MPHeadquartersActiveBranches of(HeadQuarterMembers members) {
        List<BranchMemberInfo> infos = new LinkedList<>();

        for (BranchMembers branchMembers : members.getBranches()) {
            infos.add(BranchMemberInfo.of(branchMembers));
        }

        return MPHeadquartersActiveBranches.builder()
                .branches(infos)
                .build();
    }
}
