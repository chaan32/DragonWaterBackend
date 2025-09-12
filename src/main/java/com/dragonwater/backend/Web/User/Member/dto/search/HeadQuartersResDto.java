package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.User.Member.domain.BranchMembers;
import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Builder
public class HeadQuartersResDto {
    private Role memberType;
    private String name; // 본사 명
    private String bizRegImageUrl;
    private String bizRegNumber;
    private String bizType;
    private List<String> branches;

    public static HeadQuartersResDto of(HeadQuarterMembers member){
        List<String> branches = new LinkedList<>();
        for (BranchMembers branch : member.getBranches()) {
            branches.add(branch.getBranchName());
        }
        return HeadQuartersResDto.builder()
                .name(member.getName())
                .bizType(member.getBusinessType())
                .bizRegImageUrl(member.getBizRegUrl())
                .bizRegNumber(member.getBusinessRegistrationNumber())
                .branches(branches)
                .memberType(member.getRole())
                .build();
    }
}
