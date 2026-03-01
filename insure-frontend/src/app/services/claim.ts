import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ClaimRequest {
  policyId: number;
  userId: number;
  claimAmount: number;
  claimType: string;
  incidentDate: string;
  description: string;
}

export interface ClaimResponse {
  claimId: number;
  claimNumber: string;
  policyId: number;
  userId: number;
  claimAmount: number;
  claimType: string;
  incidentDate: string;
  description: string;
  status: string;
  submittedDate: string;
  reviewedDate?: string;
  reviewedBy?: number;
  approvedAmount?: number;
  rejectionReason?: string;
}

@Injectable({
  providedIn: 'root'
})
export class ClaimService {
  private apiUrl = 'http://localhost:8383/api/claims'; // Direct to Claim Service

  constructor(private http: HttpClient) { }

  submitClaim(claimData: ClaimRequest): Observable<ClaimResponse> {
    const userId = this.getCurrentUserId();
    const payload = { 
      ...claimData, 
      userId,
      claimType: claimData.claimType.toUpperCase()
    };
    return this.http.post<ClaimResponse>(this.apiUrl, payload);
  }

  getUserClaims(): Observable<ClaimResponse[]> {
    const userId = this.getCurrentUserId();
    return this.http.get<ClaimResponse[]>(`${this.apiUrl}/user/${userId}`);
  }

  getAllClaims(): Observable<ClaimResponse[]> {
    return this.http.get<ClaimResponse[]>(this.apiUrl);
  }

  getClaimsByStatus(status: string): Observable<ClaimResponse[]> {
    return this.http.get<ClaimResponse[]>(`${this.apiUrl}/status/${status}`);
  }

  approveClaim(claimId: number, approvedAmount: number): Observable<ClaimResponse> {
    const reviewedBy = this.getCurrentUserId();
    return this.http.put<ClaimResponse>(`${this.apiUrl}/${claimId}/approve`, {
      approvedAmount,
      reviewedBy
    });
  }

  rejectClaim(claimId: number, rejectionReason: string): Observable<ClaimResponse> {
    const reviewedBy = this.getCurrentUserId();
    return this.http.put<ClaimResponse>(`${this.apiUrl}/${claimId}/reject`, {
      rejectionReason,
      reviewedBy
    });
  }

  private getCurrentUserId(): number {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user).userId || 1 : 1;
  }
}