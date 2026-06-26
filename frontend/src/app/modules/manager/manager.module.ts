import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { NgChartsModule } from 'ng2-charts';
import { ManagerRoutingModule } from './manager-routing.module';
import { ManagerDashboardComponent } from '../../components/manager-dashboard/manager-dashboard.component';

@NgModule({
  declarations: [ManagerDashboardComponent],
  imports: [
    CommonModule, ReactiveFormsModule, ManagerRoutingModule, NgChartsModule,
    MatCardModule, MatInputModule, MatButtonModule, MatTabsModule,
    MatIconModule, MatSelectModule, MatCheckboxModule, MatListModule,
    MatFormFieldModule, MatSnackBarModule
  ]
})
export class ManagerModule {}
