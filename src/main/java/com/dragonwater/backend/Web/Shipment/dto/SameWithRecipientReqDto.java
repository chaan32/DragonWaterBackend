package com.dragonwater.backend.Web.Shipment.dto;

import com.dragonwater.backend.Web.User.Member.domain.Members;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SameWithRecipientReqDto {
    private String name;
    private String phone;
    private String address;
    private String detailAddress;
    private String zipCode;
    private String email;

    public static SameWithRecipientReqDto of(Members member) {
        return SameWithRecipientReqDto.builder()
                .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .address(member.getAddress())
                .detailAddress(member.getDetailAddress())
                .zipCode(member.getZipCode())
                .build();
    }
}
