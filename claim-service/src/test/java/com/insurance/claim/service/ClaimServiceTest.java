package com.insurance.claim.service;

import com.insurance.claim.dto.ClaimDTO;
import com.insurance.claim.entity.Claim;
import com.insurance.claim.repository.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {
    
    @Mock
    private ClaimRepository claimRepository;
    
    @InjectMocks
    private ClaimServiceImpl claimService;
    
    private ClaimDTO claimDTO;
    private Claim claim;
    
    @BeforeEach
    void setUp() {
        claimDTO = new ClaimDTO();
        claimDTO.setPolicyId(1L);
        claimDTO.setUserId(1L);
        claimDTO.setClaimAmount(new BigDecimal("5000.00"));
        claimDTO.setClaimType(Claim.ClaimType.HEALTH);
        claimDTO.setIncidentDate(LocalDate.now().minusDays(5));
        claimDTO.setDescription("Medical emergency claim");
        
        claim = new Claim();
        claim.setClaimId(1L);
        claim.setClaimNumber("CLM-2024-TEST001");
        claim.setPolicyId(1L);
        claim.setUserId(1L);
        claim.setClaimAmount(new BigDecimal("5000.00"));
        claim.setClaimType(Claim.ClaimType.HEALTH);
        claim.setIncidentDate(LocalDate.now().minusDays(5));
        claim.setDescription("Medical emergency claim");
        claim.setStatus(Claim.ClaimStatus.PENDING);
    }
    
    @Test
    void submitClaim_Success() {
        // Arrange
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);
        
        // Act
        ClaimDTO result = claimService.submitClaim(claimDTO);
        
        // Assert
        assertNotNull(result);
        assertEquals(Claim.ClaimStatus.PENDING, result.getStatus());
        verify(claimRepository, times(1)).save(any(Claim.class));
    }
    
    @Test
    void getClaimById_Success() {
        // Arrange
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        
        // Act
        ClaimDTO result = claimService.getClaimById(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getClaimId());
        assertEquals("CLM-2024-TEST001", result.getClaimNumber());
    }
    
    @Test
    void getClaimsByUserId_Success() {
        // Arrange
        when(claimRepository.findByUserId(1L)).thenReturn(Arrays.asList(claim));
        
        // Act
        List<ClaimDTO> result = claimService.getClaimsByUserId(1L);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUserId());
    }
    
    @Test
    void approveClaim_Success() {
        // Arrange
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);
        
        // Act
        ClaimDTO result = claimService.approveClaim(1L, 5000.0, 2L);
        
        // Assert
        assertNotNull(result);
        verify(claimRepository, times(1)).save(any(Claim.class));
    }
    
    @Test
    void rejectClaim_Success() {
        // Arrange
        when(claimRepository.findById(1L)).thenReturn(Optional.of(claim));
        when(claimRepository.save(any(Claim.class))).thenReturn(claim);
        
        // Act
        ClaimDTO result = claimService.rejectClaim(1L, "Insufficient documentation", 2L);
        
        // Assert
        assertNotNull(result);
        verify(claimRepository, times(1)).save(any(Claim.class));
    }
}