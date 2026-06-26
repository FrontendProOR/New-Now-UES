import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LocationSearchResult } from '../models';

@Injectable({ providedIn: 'root' })
export class SearchService {

  private apiUrl = environment.apiUrl + '/search';

  constructor(private http: HttpClient) {}

  searchLocations(filters: any): Observable<LocationSearchResult[]> {
    let params = new HttpParams();
    Object.keys(filters).forEach(key => {
      if (filters[key] !== null && filters[key] !== undefined && filters[key] !== '') {
        params = params.set(key, filters[key]);
      }
    });
    return this.http.get<LocationSearchResult[]>(this.apiUrl + '/locations', { params });
  }

  downloadPdf(locationId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/locations/${locationId}/pdf`, { responseType: 'blob' });
  }
}
