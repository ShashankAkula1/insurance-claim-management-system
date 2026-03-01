import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule, CommonModule],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  registerForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  usernameAvailable = true;
  emailAvailable = true;
  checkingUsername = false;
  checkingEmail = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      fullName: ['', Validators.required],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]]
    });

    // Real-time username checking
    this.registerForm.get('username')?.valueChanges
      .pipe(debounceTime(500), distinctUntilChanged())
      .subscribe(username => {
        if (username && username.length >= 3) {
          this.checkUsernameAvailability(username);
        }
      });

    // Real-time email checking
    this.registerForm.get('email')?.valueChanges
      .pipe(debounceTime(500), distinctUntilChanged())
      .subscribe(email => {
        if (email && this.registerForm.get('email')?.valid) {
          this.checkEmailAvailability(email);
        }
      });
  }

  checkUsernameAvailability(username: string) {
    this.checkingUsername = true;
    this.authService.checkUsernameAvailability(username).subscribe({
      next: (exists) => {
        this.usernameAvailable = !exists;
        this.checkingUsername = false;
      },
      error: () => {
        this.checkingUsername = false;
      }
    });
  }

  checkEmailAvailability(email: string) {
    this.checkingEmail = true;
    this.authService.checkEmailAvailability(email).subscribe({
      next: (exists) => {
        this.emailAvailable = !exists;
        this.checkingEmail = false;
      },
      error: () => {
        this.checkingEmail = false;
      }
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      
      this.authService.register(this.registerForm.value).subscribe({
        next: (response) => {
          console.log('Registration successful:', response);
          alert('Registration successful! Please login.');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Registration error:', error);
          if (error.status === 201 || error.status === 200) {
            // Registration actually succeeded
            alert('Registration successful! Please login.');
            this.router.navigate(['/login']);
          } else {
            this.errorMessage = error.error?.message || error.message || 'Registration failed';
          }
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }
}