import { Component, OnDestroy, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { LoginRequest } from '../../../shared/models/auth.model';
import { AuthService } from '../../../core/auth/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnDestroy {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  registerForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  errorMessage: string = '';

  // Stocke les subscriptions pour un nettoyage dans ngOnDestroy
  private subscriptions: Subscription[] = [];

  onSubmit(): void {
    if (this.registerForm.valid) {
      const loginRequest: LoginRequest = {
        usernameOrEmail: this.registerForm.value.username,
        password: this.registerForm.value.password
      };

      const sub = this.authService.login(loginRequest).subscribe({
        next: (response) => {
          this.authService.saveToken(response.token);
          console.log('Login successful');
          this.errorMessage = '';
          this.router.navigate(['/articles']);
        },
        error: (error) => {
          console.error('Login failed', error);
          this.errorMessage = 'Identifiants incorrects, veuillez réessayer.';
        }
      });

      // On ajoute la subscription au tableau
      this.subscriptions.push(sub);

    } else {
      console.log('Form is invalid');
      this.errorMessage = 'Tous les champs sont requis.';
    }
  }
 // Désabonnement pour éviter les fuites mémoire
  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

}