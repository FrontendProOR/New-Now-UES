import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Location } from '../models';

@Injectable({ providedIn: 'root' })
export class LocationService {

  private apiUrl = environment.apiUrl + '/locations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Location[]> {
    return this.http.get<Location[]>(this.apiUrl);
  }

  getById(id: number): Observable<Location> {
    return this.http.get<Location>(`${this.apiUrl}/${id}`);
  }

  search(name?: string, address?: string, type?: string): Observable<Location[]> {
    let params = new HttpParams();
    if (name) params = params.set('name', name);
    if (address) params = params.set('address', address);
    if (type) params = params.set('type', type);
    return this.http.get<Location[]>(this.apiUrl, { params });
  }
}
