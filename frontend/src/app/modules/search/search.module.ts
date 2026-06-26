import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatFormFieldModule } from '@angular/material/form-field';
import { SearchRoutingModule } from './search-routing.module';
import { SearchComponent } from '../../components/search/search.component';

@NgModule({
  declarations: [SearchComponent],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule, SearchRoutingModule,
    MatCardModule, MatInputModule, MatButtonModule, MatSelectModule,
    MatIconModule, MatExpansionModule, MatFormFieldModule
  ]
})
export class SearchModule {}
