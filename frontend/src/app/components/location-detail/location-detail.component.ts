import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { LocationService } from '../../services/location.service';
import { EventService } from '../../services/event.service';
import { ReviewService } from '../../services/review.service';
import { Location, Event, Review } from '../../models';

@Component({
  selector: 'app-location-detail',
  templateUrl: './location-detail.component.html',
  styleUrls: ['./location-detail.component.scss']
})
export class LocationDetailComponent implements OnInit {
  location: Location | null = null;
  events: Event[] = [];
  reviews: Review[] = [];
  sortBy = 'date';
  sortDirection = 'desc';

  constructor(private route: ActivatedRoute,
              private locationService: LocationService,
              private eventService: EventService,
              private reviewService: ReviewService) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.locationService.getById(id).subscribe(data => this.location = data);
    this.eventService.search({ locationId: id }).subscribe(data => this.events = data);
    this.loadReviews(id);
  }

  loadReviews(locationId?: number): void {
    const id = locationId || this.location?.id;
    if (!id) return;
    this.reviewService.getByLocation(id, this.sortBy, this.sortDirection)
      .subscribe(data => this.reviews = data);
  }

  onSortChange(sortBy: string): void {
    this.sortBy = sortBy;
    this.loadReviews();
  }

  toggleDirection(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.loadReviews();
  }
}
