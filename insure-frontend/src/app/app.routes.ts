import { Routes } from '@angular/router';
import { LandingComponent } from './landing/landing';
import { RegisterComponent } from './auth/register/register';
import { LoginComponent } from './auth/login/login';
import { CustomerDashboardComponent } from './portals/customer-dashboard/customer-dashboard';
import { AdminDashboardComponent } from './portals/admin-dashboard/admin-dashboard';

export const routes: Routes = [
  { path: '', component: LandingComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'login', component: LoginComponent },
  { path: 'customer-dashboard', component: CustomerDashboardComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent },
  { path: '**', redirectTo: '' }
];
