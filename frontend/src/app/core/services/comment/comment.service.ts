import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentRequest, CommentResponse } from '../../../shared/models/comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private apiUrl = `${environment.apiUrl}/comments`;  // L'URL des commentaires

  constructor(private http: HttpClient) {}
  
  getComments(postId: number): Observable<CommentResponse[]> {
    return this.http.get<CommentResponse[]>(`${this.apiUrl}/post/${postId}`);
  }

  addComment(request: CommentRequest): Observable<CommentResponse> {
    return this.http.post<CommentResponse>(this.apiUrl, request);
  }
}
