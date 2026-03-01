package com.insurance.claim.service;

import com.insurance.claim.dto.ClaimDTO;
import com.insurance.claim.entity.Claim;

import java.util.List;

public interface ClaimService {
    
    ClaimDTO submitClaim(ClaimDTO claimDTO);
    
    ClaimDTO getClaimById(Long claimId);
    
    ClaimDTO getClaimByClaimNumber(String claimNumber);
    
    List<ClaimDTO> getAllClaims();
    
    List<ClaimDTO> getClaimsByUserId(Long userId);
    
    List<ClaimDTO> getClaimsByStatus(Claim.ClaimStatus status);
    
    ClaimDTO updateClaimStatus(Long claimId, Claim.ClaimStatus status, Long reviewedBy);
    
    ClaimDTO approveClaim(Long claimId, Double approvedAmount, Long reviewedBy);
    
    ClaimDTO rejectClaim(Long claimId, String rejectionReason, Long reviewedBy);
    
    void deleteClaim(Long claimId);
    
    Long getClaimCountByStatus(Claim.ClaimStatus status);
    
    Long getClaimCountByUser(Long userId);
}