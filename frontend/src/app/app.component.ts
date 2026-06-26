import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from './services/auth.service';
import { AuthResponse } from './models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'New Now UES';
  currentUser$: Observable<AuthResponse | null>;
  isAdmin$: Observable<boolean>;
  isManagerOrAdmin$: Observable<boolean>;

  constructor(public authService: AuthService) {
    this.currentUser$ = this.authService.currentUser$;
    this.isAdmin$ = this.currentUser$.pipe(map(u => u?.role === 'ROLE_ADMIN'));
    this.isManagerOrAdmin$ = this.currentUser$.pipe(
      map(u => u?.role === 'ROLE_ADMIN' || u?.role === 'ROLE_MANAGER')
    );
  }

  logout(): void {
    this.authService.logout();
  }
}
