import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NgChartsModule } from 'ng2-charts';
import { ManagerRoutingModule } from './manager-routing.module';
import { ManagerDashboardComponent } from '../../components/manager-dashboard/manager-dashboard.component';

@NgModule({
  declarations: [ManagerDashboardComponent],
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, ManagerRoutingModule,
    NgChartsModule, MatSnackBarModule
  ]
})
export class ManagerModule {}
