import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const requiredRole = route.data['role'] as string;
    const requiredRoles = route.data['roles'] as string[];

    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }

    if (requiredRole && this.authService.hasRole(requiredRole)) {
      return true;
    }

    if (requiredRoles && this.authService.hasAnyRole(...requiredRoles)) {
      return true;
    }

    this.router.navigate(['/']);
    return false;
  }
}
