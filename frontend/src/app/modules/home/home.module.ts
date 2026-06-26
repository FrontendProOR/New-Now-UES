import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from '../../components/home/home.component';

@NgModule({
  declarations: [HomeComponent],
  imports: [CommonModule, RouterModule, HomeRoutingModule, MatCardModule, MatChipsModule]
})
export class HomeModule {}
