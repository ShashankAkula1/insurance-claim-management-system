import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule, CommonModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      
      this.authService.login(this.loginForm.value).subscribe({
        next: (response: any) => {
          console.log('Login response:', response);
          
          // Handle different response formats
          if (response && (response.token || response.message)) {
            const token = response.token || 'temp-token';
            const username = response.username || this.loginForm.value.username;
            const email = response.email || '';
            const role = response.role || 'CUSTOMER';
            
            console.log('Detected role:', role);
            
            localStorage.setItem('token', token);
            localStorage.setItem('user', JSON.stringify({
              userId: response.userId,
              username: username,
              email: email,
              role: role
            }));
            
            // Route based on role
            console.log('Routing based on role:', role);
            if (role === 'ADMIN' || username === 'admin') {
              console.log('Navigating to admin dashboard');
              this.router.navigate(['/admin-dashboard']);
            } else {
              console.log('Navigating to customer dashboard');
              this.router.navigate(['/customer-dashboard']);
            }
          } else {
            this.errorMessage = 'Login response format error';
          }
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Login error:', error);
          if (error.status === 500 || error.status === 0) {
            this.errorMessage = 'Invalid username or password';
          } else {
            this.errorMessage = error.error?.message || error.message || 'Invalid username or password';
          }
          this.isLoading = false;
        }
      });
    }
  }
}