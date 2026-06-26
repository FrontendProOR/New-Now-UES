import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Event } from '../models';

@Injectable({ providedIn: 'root' })
export class EventService {

  private apiUrl = environment.apiUrl + '/events';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Event[]> {
    return this.http.get<Event[]>(this.apiUrl);
  }

  getTodayEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(this.apiUrl + '/today');
  }

  search(filters: any): Observable<Event[]> {
    let params = new HttpParams();
    Object.keys(filters).forEach(key => {
      if (filters[key] !== null && filters[key] !== undefined && filters[key] !== '') {
        params = params.set(key, filters[key]);
      }
    });
    return this.http.get<Event[]>(this.apiUrl, { params });
  }
}
