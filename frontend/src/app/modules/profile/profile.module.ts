import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ProfileRoutingModule } from './profile-routing.module';
import { UserProfileComponent } from '../../components/user-profile/user-profile.component';

@NgModule({
  declarations: [UserProfileComponent],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule, ProfileRoutingModule,
    MatCardModule, MatInputModule, MatButtonModule, MatTabsModule,
    MatFormFieldModule, MatSnackBarModule
  ]
})
export class ProfileModule {}
