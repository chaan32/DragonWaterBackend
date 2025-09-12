package com.dragonwater.backend.Web.Support.Claim.service;

import com.dragonwater.backend.Config.CloudStorage.CloudStorageService;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.ETC.DuplicateClaimException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.Failed.AddFailedException;
import com.dragonwater.backend.Config.Exception.New.Exception.specific.NotFound.ClaimNotFoundException;
import com.dragonwater.backend.Web.Order.domain.Orders;
import com.dragonwater.backend.Web.Order.service.interf.OrderService;
import com.dragonwater.backend.Web.Support.Claim.domain.Claims;
import com.dragonwater.backend.Web.Support.Claim.dto.ClaimResDto;
import com.dragonwater.backend.Web.Support.Claim.repository.ClaimRepository;
import com.dragonwater.backend.Web.Support.Inquiry.General.dto.GeneralInquiryReqDto;
import com.dragonwater.backend.Web.User.Member.domain.Members;
import com.dragonwater.backend.Web.User.Member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClaimServiceImpl implements ClaimService {
    private final CloudStorageService cloudStorageService;
    private final MemberService memberService;
    private final OrderService orderService;

    private final ClaimRepository claimRepository;



    @Override
    @Transactional
    public Claims addClaims(GeneralInquiryReqDto dto, List<MultipartFile> files, Long memberId) {
        try {
            Members member = memberService.getMemberById(memberId);

            Claims claims;
            if (dto.getOrderId() == null) {
                claims = addClaimExcludeOrder(member, dto);
            } else {
                Orders order = orderService.getOrdersById(dto.getOrderId());
                claims = addClaimIncludeOrder(order, member, dto);
            }

            if (files != null) {
                List<String> urls = new LinkedList<>();
                for (MultipartFile file : files) {
                    String url = cloudStorageService.uploadClaimImage(file);
                    urls.add(url);
                }
                claims.addImageUrls(urls);
            }

            claimRepository.save(claims);
            return claims;
        } catch (DuplicateClaimException e) {
            throw new DuplicateClaimException();
        } catch (Exception e) {
            throw new AddFailedException();
        }
    }



    @Override
    public List<ClaimResDto> getClaims() {
        List<ClaimResDto> dtos = new LinkedList<>();
        for (Claims claims : claimRepository.findAll()) {
            dtos.add(ClaimResDto.of(claims));
        }
        return dtos;
    }


    @Override
    @Transactional
    public void updateClaims(Long claimId, String status, String reason) {
        Claims claims = getClaimById(claimId);
        if (status.equals("rejected")) {
            claims.reject(reason);
        }
        else {
            claims.approve();
        }
    }

    @Override
    public Claims getClaimById(Long id) {

        return claimRepository.findById(id).orElseThrow(()-> new ClaimNotFoundException());
    }

    private boolean hasClaim(Orders orders) {
        return orders.getClaim() == null ? false : true;
    }

    private Claims addClaimIncludeOrder(Orders orders,  Members members, GeneralInquiryReqDto dto) throws IOException {
        if (orders.getClaim() != null) {
            throw new DuplicateClaimException();
        }
        Claims claims = Claims.of(orders, dto, members);
        orders.setClaim(claims);
        return claims;
    }

    private Claims addClaimExcludeOrder( Members members, GeneralInquiryReqDto dto) throws IOException {
        Claims claims = Claims.of(null, dto, members);
        return claims;
    }
}
