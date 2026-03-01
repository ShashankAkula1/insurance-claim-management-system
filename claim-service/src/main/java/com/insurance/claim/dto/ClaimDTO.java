package com.insurance.claim.dto;

import com.insurance.claim.entity.Claim;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClaimDTO {
    
    private Long claimId;
    private String claimNumber;
    
    @NotNull(message = "Policy ID is required")
    private Long policyId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Claim amount is required")
    @DecimalMin(value = "0.01", message = "Claim amount must be greater than 0")
    private BigDecimal claimAmount;
    
    @NotNull(message = "Claim type is required")
    private Claim.ClaimType claimType;
    
    @NotNull(message = "Incident date is required")
    @PastOrPresent(message = "Incident date cannot be in the future")
    private LocalDate incidentDate;
    
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;
    
    private Claim.ClaimStatus status;
    private BigDecimal approvedAmount;
    private String rejectionReason;
    private LocalDateTime submittedDate;
    private LocalDateTime reviewedDate;
    private Long reviewedBy;

    // Default Constructor
    public ClaimDTO() {
    }

    // All-Args Constructor
    public ClaimDTO(Long claimId, String claimNumber, Long policyId, Long userId,
                    BigDecimal claimAmount, Claim.ClaimType claimType, LocalDate incidentDate,
                    String description, Claim.ClaimStatus status, BigDecimal approvedAmount,
                    String rejectionReason, LocalDateTime submittedDate, 
                    LocalDateTime reviewedDate, Long reviewedBy) {
        this.claimId = claimId;
        this.claimNumber = claimNumber;
        this.policyId = policyId;
        this.userId = userId;
        this.claimAmount = claimAmount;
        this.claimType = claimType;
        this.incidentDate = incidentDate;
        this.description = description;
        this.status = status;
        this.approvedAmount = approvedAmount;
        this.rejectionReason = rejectionReason;
        this.submittedDate = submittedDate;
        this.reviewedDate = reviewedDate;
        this.reviewedBy = reviewedBy;
    }

    // Getters and Setters
    public Long getClaimId() {
        return claimId;
    }

    public void setClaimId(Long claimId) {
        this.claimId = claimId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public Claim.ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(Claim.ClaimType claimType) {
        this.claimType = claimType;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Claim.ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(Claim.ClaimStatus status) {
        this.status = status;
    }

    public BigDecimal getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(BigDecimal approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(LocalDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

    public LocalDateTime getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(LocalDateTime reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public Long getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Long reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
}