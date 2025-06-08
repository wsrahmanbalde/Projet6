import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  standalone: true,
  template: ''
})
export class RedirectComponent implements OnInit {
  private auth = inject(AuthService);
  private router = inject(Router);

  ngOnInit(): void {
    this.auth.checkAuth().subscribe({
      next: (isLoggedIn) => {
        this.router.navigate([isLoggedIn ? '/articles' : '/home']);
      },
      error: (err) => {
        console.error('[Erreur] Vérification de connexion :', err);
        this.router.navigate(['/home']); // Fallback
      }
    });
  }
}