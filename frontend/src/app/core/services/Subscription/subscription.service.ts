import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { SubscriptionResponse } from '../../../shared/models/subscription.model';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SubscriptionService {
  
   private readonly apiUrl = `${environment.apiUrl}/subscriptions`;

  constructor(private http: HttpClient) {}

  isSubscribed(subjectId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/check/${subjectId}`);
  }

  subscribe(subjectId: number): Observable<SubscriptionResponse> {
    return this.http.post<SubscriptionResponse>(`${this.apiUrl}/${subjectId}`, null).pipe(
      tap((res) => console.log('Abonnement réussi')),
      catchError((error) => {
        console.error('Erreur abonnement');
        return throwError(() => error);
      })
    );
  }

  unsubscribe(subjectId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${subjectId}`).pipe(
      tap(() => console.log('Désabonnement réussi')),
      catchError((error) => {
        console.error('Erreur désabonnement ');
        return throwError(() => error);
      })
    );
  }

  getMySubscriptions(): Observable<SubscriptionResponse[]> {
    return this.http.get<SubscriptionResponse[]>(this.apiUrl).pipe(
      tap((res) => console.log('Mes abonnements ')),
      catchError((error) => {
        console.error('[SubscriptionService] Erreur récupération abonnements :', error);
        return throwError(() => error);
      })
    );
  }
}
