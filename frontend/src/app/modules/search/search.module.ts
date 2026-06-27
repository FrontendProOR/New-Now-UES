import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { SearchRoutingModule } from './search-routing.module';
import { SearchComponent } from '../../components/search/search.component';

@NgModule({
  declarations: [SearchComponent],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule, SearchRoutingModule
  ]
})
export class SearchModule {}
