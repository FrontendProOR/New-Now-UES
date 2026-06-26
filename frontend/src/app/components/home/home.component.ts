import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Location, Event, Review } from '../../models';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  todayEvents: Event[] = [];
  popularLocations: Location[] = [];
  latestReviews: Review[] = [];

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<Event[]>(`${this.apiUrl}/home/today-events`).subscribe(data => this.todayEvents = data);
    this.http.get<Location[]>(`${this.apiUrl}/home/popular-locations`).subscribe(data => this.popularLocations = data);
    this.http.get<Review[]>(`${this.apiUrl}/home/latest-reviews`).subscribe(data => this.latestReviews = data);
  }
}
