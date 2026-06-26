import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Review, CreateReviewRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class ReviewService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getByLocation(locationId: number, sortBy?: string, direction?: string): Observable<Review[]> {
    let params = new HttpParams();
    if (sortBy) params = params.set('sortBy', sortBy);
    if (direction) params = params.set('direction', direction);
    return this.http.get<Review[]>(`${this.apiUrl}/locations/${locationId}/reviews`, { params });
  }

  create(locationId: number, request: CreateReviewRequest): Observable<Review> {
    return this.http.post<Review>(`${this.apiUrl}/locations/${locationId}/reviews`, request);
  }
}
