import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: FormGroup;

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router,
              private snackBar: MatSnackBar) {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      address: ['']
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) return;
    this.authService.register(this.registerForm.value).subscribe({
      next: (msg) => {
        this.snackBar.open(msg, 'Close', { duration: 5000 });
        this.router.navigate(['/login']);
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Registration failed', 'Close', { duration: 3000 })
    });
  }
}
