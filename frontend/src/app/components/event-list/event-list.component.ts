import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { EventService } from '../../services/event.service';
import { LocationService } from '../../services/location.service';
import { Event, Location } from '../../models';

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {
  events: Event[] = [];
  locations: Location[] = [];
  filterForm: FormGroup;

  constructor(private eventService: EventService,
              private locationService: LocationService,
              private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      type: [''],
      locationId: [''],
      address: [''],
      minPrice: [''],
      maxPrice: [''],
      dateFrom: [''],
      dateTo: ['']
    });
  }

  ngOnInit(): void {
    this.eventService.getTodayEvents().subscribe(data => this.events = data);
    this.locationService.getAll().subscribe(data => this.locations = data);
  }

  onFilter(): void {
    const filters: any = {};
    const val = this.filterForm.value;
    if (val.type) filters.type = val.type;
    if (val.locationId) filters.locationId = val.locationId;
    if (val.address) filters.address = val.address;
    if (val.minPrice) filters.minPrice = val.minPrice;
    if (val.maxPrice) filters.maxPrice = val.maxPrice;
    if (val.dateFrom) filters.dateFrom = this.formatDate(val.dateFrom);
    if (val.dateTo) filters.dateTo = this.formatDate(val.dateTo);
    this.eventService.search(filters).subscribe(data => this.events = data);
  }

  onClear(): void {
    this.filterForm.reset();
    this.eventService.getTodayEvents().subscribe(data => this.events = data);
  }

  private formatDate(date: any): string {
    if (typeof date === 'string') return date;
    const d = new Date(date);
    return d.toISOString().split('T')[0];
  }
}
