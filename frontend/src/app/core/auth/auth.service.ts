import { AuthResponse, LoginRequest, RegisterRequest, UserRequest, UserResponse } from './../../shared/models/auth.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { computed, inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly loginUrl = `${environment.apiUrl}/auth/login`;
  private readonly registerUrl = `${environment.apiUrl}/auth/register`;
  private readonly userUrl = `${environment.apiUrl}/users/me`;
  
  private http = inject(HttpClient);

  private token = signal<string | null>(this.getTokenFromStorage());

  readonly isLoggedIn = computed(() => !!this.token());

  login(loginRequest: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.loginUrl, loginRequest);
  }

  register(registerRequest: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(this.registerUrl, registerRequest);
  }

  getProfile(): Observable<UserResponse> {
    return this.http.get<UserResponse>(this.userUrl);
  }

  updateProfile(data: UserRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(this.userUrl, data);
  }

  saveToken(token: string): void {
    localStorage.setItem('jwt', token);
    this.token.set(token);
  }

  getToken(): string | null {
    const token = localStorage.getItem('jwt');
    return token;
  }

  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  logout(): void {
    console.log('[AuthService] Déconnexion');
    localStorage.removeItem('jwt');
    this.token.set(null);
  }

  private getTokenFromStorage(): string | null {
    const token = localStorage.getItem('jwt');
    return token;
  }

  checkAuth(): Observable<boolean> {
  return new Observable<boolean>((observer) => {
    const token = this.getToken();
    if (!token) {
      observer.next(false);
      observer.complete();
      return;
    }

    this.http.get<UserResponse>(this.userUrl, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${token}`)
    }).subscribe({
      next: () => {
        observer.next(true);
        observer.complete();
      },
      error: () => {
        this.logout(); // Token invalide, on force la déconnexion
        observer.next(false);
        observer.complete();
      }
    });
  });
}
}
