import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../../services/user.service';
import { User, Review, Location } from '../../models';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  user: User | null = null;
  reviews: Review[] = [];
  managedLocations: Location[] = [];
  profileForm!: FormGroup;
  passwordForm: FormGroup;
  selectedImage: File | null = null;
  activeTab = 0;

  constructor(private userService: UserService,
              private fb: FormBuilder,
              private snackBar: MatSnackBar) {
    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadProfile();
    this.userService.getMyReviews().subscribe(data => this.reviews = data);
    this.userService.getManagedLocations().subscribe(data => this.managedLocations = data);
  }

  loadProfile(): void {
    this.userService.getProfile().subscribe(user => {
      this.user = user;
      this.profileForm = this.fb.group({
        name: [user.name || ''],
        phoneNumber: [user.phoneNumber || ''],
        birthday: [user.birthday || ''],
        address: [user.address || ''],
        city: [user.city || '']
      });
    });
  }

  onImageSelected(event: any): void {
    this.selectedImage = event.target.files[0];
  }

  onUpdateProfile(): void {
    const formData = new FormData();
    const val = this.profileForm.value;
    if (val.name) formData.append('name', val.name);
    if (val.phoneNumber) formData.append('phoneNumber', val.phoneNumber);
    if (val.birthday) formData.append('birthday', val.birthday);
    if (val.address) formData.append('address', val.address);
    if (val.city) formData.append('city', val.city);
    if (this.selectedImage) formData.append('image', this.selectedImage);

    this.userService.updateProfile(formData).subscribe({
      next: (user) => {
        this.user = user;
        this.snackBar.open('Profile updated', 'Close', { duration: 3000 });
      },
      error: () => this.snackBar.open('Failed to update profile', 'Close', { duration: 3000 })
    });
  }

  onChangePassword(): void {
    if (this.passwordForm.invalid) return;
    this.userService.changePassword(this.passwordForm.value).subscribe({
      next: () => {
        this.snackBar.open('Password changed. Check your email.', 'Close', { duration: 3000 });
        this.passwordForm.reset();
      },
      error: (err) => this.snackBar.open(err.error?.error || 'Failed to change password', 'Close', { duration: 3000 })
    });
  }
}
