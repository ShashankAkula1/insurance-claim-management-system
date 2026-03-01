package com.insurance.claim.repository;

import com.insurance.claim.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {
    
    Optional<Claim> findByClaimNumber(String claimNumber);
    
    List<Claim> findByUserId(Long userId);
    
    List<Claim> findByPolicyId(Long policyId);
    
    List<Claim> findByStatus(Claim.ClaimStatus status);
    
    List<Claim> findByUserIdAndStatus(Long userId, Claim.ClaimStatus status);
    
    Long countByStatus(Claim.ClaimStatus status);
    
    Long countByUserId(Long userId);
}