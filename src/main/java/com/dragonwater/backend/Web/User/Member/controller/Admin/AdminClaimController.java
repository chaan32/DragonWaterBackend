package com.dragonwater.backend.Web.User.Member.controller.Admin;

import com.dragonwater.backend.Web.Support.Claim.dto.ClaimRejectionReqDto;
import com.dragonwater.backend.Web.Support.Claim.dto.ClaimResDto;
import com.dragonwater.backend.Web.Support.Claim.service.ClaimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/claims")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminClaimController {

    private final ClaimService claimService;

    @GetMapping
    public ResponseEntity<?> getAllClaims() {
        List<ClaimResDto> claims =
                claimService.getClaims();
        return ResponseEntity.ok(claims);
    }

    @PostMapping("/{claimId}/{status}")
    public ResponseEntity<?> rejectOrApproveClaims(@PathVariable Long claimId,
                                                   @PathVariable String status,
                                                   @RequestBody(required = false) ClaimRejectionReqDto dto) {
        String reason = "";
        if (status.equals("rejected")) {
            reason+=dto.getReason();
        }

        claimService.updateClaims(claimId, status, reason);
        log.info("claimId : {} status : {}", claimId, status);
        return ResponseEntity.ok().build();
    }
}
