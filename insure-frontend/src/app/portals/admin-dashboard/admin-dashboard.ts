import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClaimService, ClaimResponse } from '../../services/claim';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css']
})
export class AdminDashboardComponent implements OnInit {
  currentUser: any;
  selectedStatus = '';
  selectedClaim: ClaimResponse | null = null;
  approvedAmount = 0;
  rejectionReason = '';
  allClaims: ClaimResponse[] = [];
  filteredClaims: ClaimResponse[] = [];
  isLoading = false;
  
  adminStats = {
    totalClaims: 0,
    pendingClaims: 0,
    approvedToday: 0,
    rejectedToday: 0
  };

  constructor(
    private router: Router,
    private claimService: ClaimService
  ) {}

  ngOnInit() {
    const user = localStorage.getItem('user');
    if (user) {
      this.currentUser = JSON.parse(user);
      if (this.currentUser.role !== 'ADMIN') {
        this.router.navigate(['/customer-dashboard']);
      } else {
        this.loadAllClaims();
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  loadAllClaims() {
    this.isLoading = true;
    this.claimService.getAllClaims().subscribe({
      next: (claims) => {
        this.allClaims = claims;
        this.filteredClaims = [...claims];
        this.updateStats();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading claims:', error);
        this.isLoading = false;
      }
    });
  }

  updateStats() {
    this.adminStats.totalClaims = this.allClaims.length;
    this.adminStats.pendingClaims = this.allClaims.filter(c => c.status === 'PENDING').length;
    
    const today = new Date().toDateString();
    this.adminStats.approvedToday = this.allClaims.filter(c => 
      c.status === 'APPROVED' && new Date(c.reviewedDate || '').toDateString() === today
    ).length;
    this.adminStats.rejectedToday = this.allClaims.filter(c => 
      c.status === 'REJECTED' && new Date(c.reviewedDate || '').toDateString() === today
    ).length;
  }

  filterClaims() {
    if (this.selectedStatus) {
      this.filteredClaims = this.allClaims.filter(claim => claim.status === this.selectedStatus);
    } else {
      this.filteredClaims = [...this.allClaims];
    }
  }

  viewClaimDetails(claim: ClaimResponse) {
    this.selectedClaim = claim;
    this.approvedAmount = claim.claimAmount;
  }

  approveClaim(claim: ClaimResponse) {
    this.selectedClaim = claim;
    this.approvedAmount = claim.claimAmount;
  }

  rejectClaim(claim: ClaimResponse) {
    this.selectedClaim = claim;
  }

  processApproval() {
    if (this.selectedClaim) {
      this.claimService.approveClaim(this.selectedClaim.claimId, this.selectedClaim.claimAmount).subscribe({
        next: (response) => {
          alert('Claim approved successfully!');
          this.loadAllClaims();
          this.selectedClaim = null;
        },
        error: (error) => {
          console.error('Error approving claim:', error);
          alert('Error approving claim. Please try again.');
        }
      });
    }
  }

  processRejection() {
    if (this.selectedClaim && this.rejectionReason) {
      this.claimService.rejectClaim(this.selectedClaim.claimId, this.rejectionReason).subscribe({
        next: (response) => {
          alert('Claim rejected!');
          this.loadAllClaims();
          this.selectedClaim = null;
          this.rejectionReason = '';
        },
        error: (error) => {
          console.error('Error rejecting claim:', error);
          alert('Error rejecting claim. Please try again.');
        }
      });
    }
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.router.navigate(['/']);
  }
}