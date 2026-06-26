import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LocationListComponent } from '../../components/location-list/location-list.component';
import { LocationDetailComponent } from '../../components/location-detail/location-detail.component';
import { CreateReviewComponent } from '../../components/create-review/create-review.component';

const routes: Routes = [
  { path: '', component: LocationListComponent },
  { path: ':id', component: LocationDetailComponent },
  { path: ':locationId/review', component: CreateReviewComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LocationRoutingModule {}
