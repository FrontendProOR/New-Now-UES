import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminDashboardComponent } from '../../components/admin-dashboard/admin-dashboard.component';

@NgModule({
  declarations: [AdminDashboardComponent],
  imports: [
    CommonModule, ReactiveFormsModule, AdminRoutingModule,
    MatCardModule, MatInputModule, MatButtonModule, MatTabsModule,
    MatIconModule, MatFormFieldModule, MatSnackBarModule
  ]
})
export class AdminModule {}
