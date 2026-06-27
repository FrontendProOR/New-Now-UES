import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Event, Review, Location } from '../models';
import { Analytics } from '../models';

@Injectable({ providedIn: 'root' })
export class ManagerService {

  private apiUrl = environment.apiUrl + '/manager';

  constructor(private http: HttpClient) {}

  createEvent(locationId: number, formData: FormData): Observable<Event> {
    return this.http.post<Event>(`${this.apiUrl}/locations/${locationId}/events`, formData);
  }

  updateEvent(id: number, formData: FormData): Observable<Event> {
    return this.http.put<Event>(`${this.apiUrl}/events/${id}`, formData);
  }

  deleteEvent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/events/${id}`);
  }

  getLocationEvents(locationId: number): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/locations/${locationId}/events`);
  }

  getLocationDetails(locationId: number): Observable<Location> {
    return this.http.get<Location>(`${this.apiUrl}/locations/${locationId}`);
  }

  getLocationReviews(locationId: number, sortBy?: string, direction?: string): Observable<Review[]> {
    let params = new HttpParams();
    if (sortBy) params = params.set('sortBy', sortBy);
    if (direction) params = params.set('direction', direction);
    return this.http.get<Review[]>(`${this.apiUrl}/locations/${locationId}/reviews`, { params });
  }

  hideReview(id: number): Observable<Review> {
    return this.http.put<Review>(`${this.apiUrl}/reviews/${id}/hide`, {});
  }

  deleteReview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/reviews/${id}`);
  }

  updateLocation(id: number, data: { address?: string; type?: string; description?: string }): Observable<Location> {
    return this.http.put<Location>(`${this.apiUrl}/locations/${id}`, data);
  }

  getAnalytics(locationId: number, period: string, dateFrom?: string, dateTo?: string): Observable<Analytics> {
    let params = new HttpParams().set('period', period);
    if (dateFrom) params = params.set('dateFrom', dateFrom);
    if (dateTo) params = params.set('dateTo', dateTo);
    return this.http.get<Analytics>(`${this.apiUrl}/locations/${locationId}/analytics`, { params });
  }
}
