import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { LocationRoutingModule } from './location-routing.module';
import { LocationListComponent } from '../../components/location-list/location-list.component';
import { LocationDetailComponent } from '../../components/location-detail/location-detail.component';
import { CreateReviewComponent } from '../../components/create-review/create-review.component';

@NgModule({
  declarations: [LocationListComponent, LocationDetailComponent, CreateReviewComponent],
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule, RouterModule, LocationRoutingModule,
    MatSnackBarModule
  ]
})
export class LocationModule {}
