import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AccountRequest, Location } from '../models';

export interface ManagerDto {
  userId: number;
  email: string;
  name: string;
  startDate: string;
}

@Injectable({ providedIn: 'root' })
export class AdminService {

  private apiUrl = environment.apiUrl + '/admin';

  constructor(private http: HttpClient) {}

  getAccountRequests(): Observable<AccountRequest[]> {
    return this.http.get<AccountRequest[]>(this.apiUrl + '/account-requests');
  }

  getPendingRequests(): Observable<AccountRequest[]> {
    return this.http.get<AccountRequest[]>(this.apiUrl + '/account-requests/pending');
  }

  acceptRequest(id: number): Observable<AccountRequest> {
    return this.http.put<AccountRequest>(`${this.apiUrl}/account-requests/${id}/accept`, {});
  }

  rejectRequest(id: number, reason?: string): Observable<AccountRequest> {
    return this.http.put<AccountRequest>(`${this.apiUrl}/account-requests/${id}/reject`, { reason });
  }

  createLocation(formData: FormData): Observable<Location> {
    return this.http.post<Location>(this.apiUrl + '/locations', formData);
  }

  updateLocation(id: number, formData: FormData): Observable<Location> {
    return this.http.put<Location>(`${this.apiUrl}/locations/${id}`, formData);
  }

  deleteLocation(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/locations/${id}`);
  }

  uploadPdf(locationId: number, formData: FormData): Observable<string> {
    return this.http.post(`${this.apiUrl}/locations/${locationId}/pdf`, formData, { responseType: 'text' });
  }

  getManagers(locationId: number): Observable<ManagerDto[]> {
    return this.http.get<ManagerDto[]>(`${this.apiUrl}/locations/${locationId}/managers`);
  }

  assignManager(locationId: number, email: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/locations/${locationId}/managers`, { email }, { responseType: 'text' });
  }

  removeManager(locationId: number, userId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/locations/${locationId}/managers/${userId}`);
  }
}
