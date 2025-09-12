package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.User.Member.domain.HeadQuarterMembers;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HQInformResDto {
    private Long id;
    private String name;
    private String businessNumber;

    public static HQInformResDto of(HeadQuarterMembers members) {
        return HQInformResDto.builder()
                .id(members.getId())
                .name(members.getName())
                .businessNumber(members.getBusinessRegistrationNumber())
                .build();
    }
}
