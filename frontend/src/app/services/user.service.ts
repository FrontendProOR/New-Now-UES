import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { User, ChangePasswordRequest } from '../models';
import { Location } from '../models';
import { Review } from '../models';

@Injectable({ providedIn: 'root' })
export class UserService {

  private apiUrl = environment.apiUrl + '/users';

  constructor(private http: HttpClient) {}

  getProfile(): Observable<User> {
    return this.http.get<User>(this.apiUrl + '/profile');
  }

  updateProfile(formData: FormData): Observable<User> {
    return this.http.put<User>(this.apiUrl + '/profile', formData);
  }

  changePassword(request: ChangePasswordRequest): Observable<string> {
    return this.http.put(this.apiUrl + '/change-password', request, { responseType: 'text' });
  }

  getMyReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(this.apiUrl + '/reviews');
  }

  getManagedLocations(): Observable<Location[]> {
    return this.http.get<Location[]>(this.apiUrl + '/managed-locations');
  }
}
