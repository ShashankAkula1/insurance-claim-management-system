import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ClaimService, ClaimRequest, ClaimResponse } from '../../services/claim';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-dashboard.html',
  styleUrls: ['./customer-dashboard.css']
})
export class CustomerDashboardComponent implements OnInit {
  currentUser: any;
  showClaimForm = false;
  showClaimsList = false;
  claimForm: FormGroup;
  userClaims: ClaimResponse[] = [];
  isLoading = false;
  today: string;
  submitted = false;
  
  userStats = {
    totalClaims: 0,
    pendingClaims: 0,
    approvedClaims: 0,
    rejectedClaims: 0
  };

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private claimService: ClaimService
  ) {
    this.today = new Date().toISOString().split('T')[0];
    this.claimForm = this.fb.group({
      claimType: ['HEALTH', Validators.required],
      claimAmount: ['', [Validators.required, Validators.min(1)]],
      incidentDate: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(10)]],
      medicalReport: [false, Validators.requiredTrue],
      policyCopy: [false, Validators.requiredTrue],
      identityProof: [false, Validators.requiredTrue]
    });
  }

  ngOnInit() {
    const user = localStorage.getItem('user');
    if (user) {
      this.currentUser = JSON.parse(user);
      this.loadUserClaims();
    } else {
      this.router.navigate(['/login']);
    }
  }

  loadUserClaims() {
    this.claimService.getUserClaims().subscribe({
      next: (claims) => {
        this.userClaims = claims;
        this.updateStats();
      },
      error: (error) => {
        console.error('Error loading claims:', error);
      }
    });
  }

  updateStats() {
    this.userStats.totalClaims = this.userClaims.length;
    this.userStats.pendingClaims = this.userClaims.filter(c => c.status === 'PENDING').length;
    this.userStats.approvedClaims = this.userClaims.filter(c => c.status === 'APPROVED').length;
    this.userStats.rejectedClaims = this.userClaims.filter(c => c.status === 'REJECTED').length;
  }

  allDocumentsChecked(): boolean {
    return this.claimForm.get('medicalReport')?.value && 
           this.claimForm.get('policyCopy')?.value && 
           this.claimForm.get('identityProof')?.value;
  }

  submitClaim() {
    this.submitted = true;
    if (this.claimForm.valid) {
      this.isLoading = true;
      const claimData: ClaimRequest = {
        policyId: 1,
        claimAmount: this.claimForm.value.claimAmount,
        claimType: this.claimForm.value.claimType,
        incidentDate: this.claimForm.value.incidentDate,
        description: this.claimForm.value.description,
        userId: 1 // Will be set by service
      };
      
      console.log('Submitting claim:', claimData);
      
      this.claimService.submitClaim(claimData).subscribe({
        next: (response) => {
          console.log('Claim submitted successfully:', response);
          alert('Claim submitted successfully!');
          this.showClaimForm = false;
          this.claimForm.reset();
          this.submitted = false;
          this.claimForm.patchValue({ 
            claimType: 'HEALTH',
            medicalReport: false,
            policyCopy: false,
            identityProof: false
          });
          this.loadUserClaims();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error submitting claim:', error);
          alert('Error submitting claim: ' + (error.error?.message || error.message || 'Please try again.'));
          this.isLoading = false;
        }
      });
    } else {
      Object.keys(this.claimForm.controls).forEach(key => {
        this.claimForm.get(key)?.markAsTouched();
      });
    }
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.router.navigate(['/']);
  }
}