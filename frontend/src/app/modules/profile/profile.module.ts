import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ProfileRoutingModule } from './profile-routing.module';
import { UserProfileComponent } from '../../components/user-profile/user-profile.component';

@NgModule({
  declarations: [UserProfileComponent],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule, ProfileRoutingModule,
    MatSnackBarModule
  ]
})
export class ProfileModule {}
