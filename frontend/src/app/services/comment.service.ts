import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Comment } from '../models';

@Injectable({ providedIn: 'root' })
export class CommentService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCommentTree(reviewId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/reviews/${reviewId}/comments`);
  }

  createComment(reviewId: number, text: string): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/reviews/${reviewId}/comments`, { text });
  }

  createReply(commentId: number, text: string): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/comments/${commentId}/replies`, { text });
  }
}
