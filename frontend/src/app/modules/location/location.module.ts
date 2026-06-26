import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatFormFieldModule } from '@angular/material/form-field';
import { LocationRoutingModule } from './location-routing.module';
import { LocationListComponent } from '../../components/location-list/location-list.component';
import { LocationDetailComponent } from '../../components/location-detail/location-detail.component';
import { CreateReviewComponent } from '../../components/create-review/create-review.component';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@NgModule({
  declarations: [LocationListComponent, LocationDetailComponent, CreateReviewComponent],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule, LocationRoutingModule,
    MatCardModule, MatInputModule, MatButtonModule, MatIconModule,
    MatChipsModule, MatFormFieldModule, MatSelectModule, MatSnackBarModule
  ]
})
export class LocationModule {}
