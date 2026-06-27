import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { AdminRoutingModule } from './admin-routing.module';
import { AdminDashboardComponent } from '../../components/admin-dashboard/admin-dashboard.component';

@NgModule({
  declarations: [AdminDashboardComponent],
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, AdminRoutingModule,
    MatSnackBarModule
  ]
})
export class AdminModule {}
