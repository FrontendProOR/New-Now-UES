import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { EventRoutingModule } from './event-routing.module';
import { EventListComponent } from '../../components/event-list/event-list.component';

@NgModule({
  declarations: [EventListComponent],
  imports: [
    CommonModule, ReactiveFormsModule, RouterModule, EventRoutingModule
  ]
})
export class EventModule {}
