import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostRequest, PostResponse } from '../../../shared/models/post.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private readonly apiUrl = `${environment.apiUrl}/posts`;

  constructor(private http: HttpClient) {}

  getAll(sortOrder: string): Observable<PostResponse[]> {
    return this.http.get<PostResponse[]>(`${this.apiUrl}/${sortOrder}`);
  }

  getById(id: number): Observable<PostResponse> {
    return this.http.get<PostResponse>(`${this.apiUrl}/${id}`);
  }

  create(post: PostRequest): Observable<PostResponse> {
    return this.http.post<PostResponse>(this.apiUrl, post);
  }

  update(id: number, post: PostRequest): Observable<PostResponse> {
    return this.http.put<PostResponse>(`${this.apiUrl}/${id}`, post);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
