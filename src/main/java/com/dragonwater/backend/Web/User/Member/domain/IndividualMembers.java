package com.dragonwater.backend.Web.User.Member.domain;

import com.dragonwater.backend.Web.Shop.Product.domain.Products;
import com.dragonwater.backend.Web.User.Member.dto.register.IndividualMbRegReqDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("INDIVIDUAL")
@SuperBuilder
public class IndividualMembers extends Members{

    @Builder.Default
    private BigDecimal memberShipPoints = BigDecimal.ZERO;

    public static IndividualMembers of(IndividualMbRegReqDto dto, String encodedPassword) {
        return IndividualMembers.builder()
                .loginId(dto.getId())
                .loginPw(encodedPassword)
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .detailAddress(dto.getDetailAddress())
                .role(Role.INDIVIDUAL) // 역할 설정
                .zipCode(dto.getPostalCode())
                .build();
    }
    public void addPoints(BigDecimal productPrice){

        BigDecimal result = productPrice.multiply(BigDecimal.valueOf(0.05));
        BigDecimal intResult = result.setScale(0, RoundingMode.DOWN);

        this.memberShipPoints = this.memberShipPoints.add(intResult);
    }
    public void subPoints(BigDecimal memberShipPoints){
        this.memberShipPoints = this.memberShipPoints.subtract(memberShipPoints);
    }
}
