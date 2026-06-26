import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { EventRoutingModule } from './event-routing.module';
import { EventListComponent } from '../../components/event-list/event-list.component';

@NgModule({
  declarations: [EventListComponent],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule, EventRoutingModule,
    MatCardModule, MatInputModule, MatButtonModule, MatSelectModule,
    MatDatepickerModule, MatNativeDateModule, MatFormFieldModule
  ]
})
export class EventModule {}
