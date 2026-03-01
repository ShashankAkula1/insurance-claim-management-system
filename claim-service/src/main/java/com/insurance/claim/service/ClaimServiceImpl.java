package com.insurance.claim.service;

import com.insurance.claim.dto.ClaimDTO;
import com.insurance.claim.entity.Claim;
import com.insurance.claim.repository.ClaimRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;

    // Constructor Injection (without Lombok)
    public ClaimServiceImpl(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Override
    @Transactional
    public ClaimDTO submitClaim(ClaimDTO claimDTO) {
        Claim claim = new Claim();

        // Generate unique claim number
        String claimNumber = "CLM-" + LocalDateTime.now().getYear() + "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        claim.setClaimNumber(claimNumber);

        claim.setPolicyId(claimDTO.getPolicyId());
        claim.setUserId(claimDTO.getUserId());
        claim.setClaimAmount(claimDTO.getClaimAmount());
        claim.setClaimType(claimDTO.getClaimType());
        claim.setIncidentDate(claimDTO.getIncidentDate());
        claim.setDescription(claimDTO.getDescription());
        claim.setStatus(Claim.ClaimStatus.PENDING);

        Claim savedClaim = claimRepository.save(claim);
        return convertToDTO(savedClaim);
    }

    @Override
    public ClaimDTO getClaimById(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        return convertToDTO(claim);
    }

    @Override
    public ClaimDTO getClaimByClaimNumber(String claimNumber) {
        Claim claim = claimRepository.findByClaimNumber(claimNumber)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        return convertToDTO(claim);
    }

    @Override
    public List<ClaimDTO> getAllClaims() {
        return claimRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimDTO> getClaimsByUserId(Long userId) {
        return claimRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ClaimDTO> getClaimsByStatus(Claim.ClaimStatus status) {
        return claimRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClaimDTO updateClaimStatus(Long claimId, Claim.ClaimStatus status, Long reviewedBy) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        claim.setStatus(status);
        claim.setReviewedDate(LocalDateTime.now());
        claim.setReviewedBy(reviewedBy);

        Claim updatedClaim = claimRepository.save(claim);
        return convertToDTO(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDTO approveClaim(Long claimId, Double approvedAmount, Long reviewedBy) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        claim.setStatus(Claim.ClaimStatus.APPROVED);
        claim.setReviewedDate(LocalDateTime.now());
        claim.setReviewedBy(reviewedBy);

        Claim updatedClaim = claimRepository.save(claim);
        return convertToDTO(updatedClaim);
    }

    @Override
    @Transactional
    public ClaimDTO rejectClaim(Long claimId, String rejectionReason, Long reviewedBy) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found"));

        claim.setStatus(Claim.ClaimStatus.REJECTED);
        claim.setRejectionReason(rejectionReason);
        claim.setReviewedDate(LocalDateTime.now());
        claim.setReviewedBy(reviewedBy);

        Claim updatedClaim = claimRepository.save(claim);
        return convertToDTO(updatedClaim);
    }

    @Override
    @Transactional
    public void deleteClaim(Long claimId) {
        if (!claimRepository.existsById(claimId)) {
            throw new RuntimeException("Claim not found");
        }
        claimRepository.deleteById(claimId);
    }

    @Override
    public Long getClaimCountByStatus(Claim.ClaimStatus status) {
        return claimRepository.countByStatus(status);
    }

    @Override
    public Long getClaimCountByUser(Long userId) {
        return claimRepository.countByUserId(userId);
    }

    private ClaimDTO convertToDTO(Claim claim) {
        ClaimDTO dto = new ClaimDTO();
        dto.setClaimId(claim.getClaimId());
        dto.setClaimNumber(claim.getClaimNumber());
        dto.setPolicyId(claim.getPolicyId());
        dto.setUserId(claim.getUserId());
        dto.setClaimAmount(claim.getClaimAmount());
        dto.setClaimType(claim.getClaimType());
        dto.setIncidentDate(claim.getIncidentDate());
        dto.setDescription(claim.getDescription());
        dto.setStatus(claim.getStatus());
        dto.setRejectionReason(claim.getRejectionReason());
        dto.setSubmittedDate(claim.getSubmittedDate());
        dto.setReviewedDate(claim.getReviewedDate());
        dto.setReviewedBy(claim.getReviewedBy());
        return dto;
    }
}
