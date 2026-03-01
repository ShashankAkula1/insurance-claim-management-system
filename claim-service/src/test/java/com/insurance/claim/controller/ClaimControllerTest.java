package com.insurance.claim.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.claim.dto.ClaimDTO;
import com.insurance.claim.entity.Claim;
import com.insurance.claim.service.ClaimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClaimController.class)
class ClaimControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ClaimService claimService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private ClaimDTO claimDTO;
    
    @BeforeEach
    void setUp() {
        claimDTO = new ClaimDTO();
        claimDTO.setClaimId(1L);
        claimDTO.setClaimNumber("CLM-2024-TEST001");
        claimDTO.setPolicyId(1L);
        claimDTO.setUserId(1L);
        claimDTO.setClaimAmount(new BigDecimal("5000.00"));
        claimDTO.setClaimType(Claim.ClaimType.HEALTH);
        claimDTO.setIncidentDate(LocalDate.now().minusDays(5));
        claimDTO.setDescription("Medical emergency claim");
        claimDTO.setStatus(Claim.ClaimStatus.PENDING);
    }
    
    @Test
    void submitClaim_Success() throws Exception {
        // Arrange
        when(claimService.submitClaim(any(ClaimDTO.class))).thenReturn(claimDTO);
        
        // Act & Assert
        mockMvc.perform(post("/api/claims")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.claimNumber").value("CLM-2024-TEST001"));
    }
    
    @Test
    void getClaimById_Success() throws Exception {
        // Arrange
        when(claimService.getClaimById(1L)).thenReturn(claimDTO);
        
        // Act & Assert
        mockMvc.perform(get("/api/claims/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimId").value(1))
                .andExpect(jsonPath("$.claimNumber").value("CLM-2024-TEST001"));
    }
    
    @Test
    void getAllClaims_Success() throws Exception {
        // Arrange
        when(claimService.getAllClaims()).thenReturn(Arrays.asList(claimDTO));
        
        // Act & Assert
        mockMvc.perform(get("/api/claims"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claimId").value(1));
    }
}