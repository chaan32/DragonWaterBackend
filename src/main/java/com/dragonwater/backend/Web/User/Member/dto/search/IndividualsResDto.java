package com.dragonwater.backend.Web.User.Member.dto.search;

import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.dto.OrderMembersResDto;
import com.dragonwater.backend.Web.User.Member.domain.IndividualMembers;
import com.dragonwater.backend.Web.User.Member.domain.Role;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
public class IndividualsResDto {
    private String name;
    private BigDecimal memberShipPoints;

    public static IndividualsResDto of(IndividualMembers members) {
        return IndividualsResDto.builder()
                .name(members.getName())
                .memberShipPoints(members.getMemberShipPoints())
                .build();
    }
}
