import { SubjectRequest, SubjectResponse } from './../../../shared/models/subject.model';
import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  private readonly apiUrl = `${environment.apiUrl}/subjects`;

  http = inject(HttpClient);

  getSubjects(): Observable<SubjectResponse[]> {
    return this.http.get<SubjectResponse[]>(this.apiUrl).pipe(
      tap((res) => {
        console.log('Réponse reçue :');
      }),
      catchError((error) => {
        console.error('[SubjectService] Erreur lors de la requête :', error);
        return throwError(() => error);
      })
    );
  }

  getSubjectById(id: number) {
    return this.http.get<SubjectResponse>(`${this.apiUrl}/${id}`);
  }

  getSubjectByName(name: string) {
    const encodedName = encodeURIComponent(name);
    return this.http.get<SubjectResponse>(`${this.apiUrl}/name/${encodedName}`);
  }

  createSubject(subject: SubjectRequest): Observable<SubjectResponse> {
    return this.http.post<SubjectResponse>(this.apiUrl, subject).pipe(
      tap(res => console.log('[SubjectService] Sujet créé :', res)),
      catchError(error => {
        console.error('[SubjectService] Erreur création sujet :', error);
        return throwError(() => error);
      })
    );
  }
  
  updateSubject(id: number, subject: SubjectRequest): Observable<SubjectResponse> {
    return this.http.put<SubjectResponse>(`${this.apiUrl}/${id}`, subject).pipe(
      tap(res => console.log('[SubjectService] Sujet modifié :', res)),
      catchError(error => {
        console.error('[SubjectService] Erreur modification sujet :', error);
        return throwError(() => error);
      })
    );
  }
  
  deleteSubject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => console.log('[SubjectService] Sujet supprimé :', id)),
      catchError(error => {
        console.error('[SubjectService] Erreur suppression sujet :', error);
        return throwError(() => error);
      })
    );
  }

}
