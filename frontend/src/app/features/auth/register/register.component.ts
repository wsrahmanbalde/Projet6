import { CommonModule } from '@angular/common';
import { Component, OnDestroy, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { RegisterRequest } from '../../../shared/models/auth.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnDestroy {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);
  private subscriptions: Subscription[] = [];
  errorMessage: string | null = null;

  registerForm: FormGroup = this.fb.group({
    username: ['', Validators.required],
    email: [
      '',
      [
        Validators.required,
        Validators.email,
        Validators.pattern(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-z]{2,}$/)
      ]
    ],
    password: [
      '',
      [
        Validators.required,
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_]).{8,}$/)
      ]
    ]
  });

  emailTouched = false;
  showPasswordCriteria = false;
  formSubmitted = false;

  passwordCriteria = {
    length: false,
    uppercase: false,
    lowercase: false,
    digit: false,
    specialChar: false
  };

  get emailInvalid(): boolean {
    const control = this.registerForm.get('email');
    return (
      this.emailTouched &&
      control?.invalid &&
      (control.errors?.['email'] || control.errors?.['pattern'])
    );
  }

  onPasswordInput(): void {
    const password = this.registerForm.get('password')?.value || '';
    this.showPasswordCriteria = password.length > 0;

    this.passwordCriteria = {
      length: password.length >= 8,
      uppercase: /[A-Z]/.test(password),
      lowercase: /[a-z]/.test(password),
      digit: /[0-9]/.test(password),
      specialChar: /[\W_]/.test(password)
    };
  }

  onSubmit(): void {
    this.formSubmitted = true;

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const registerData: RegisterRequest = this.registerForm.value;

    const sub = this.authService.register(registerData).subscribe({
      next: (response) => {
        this.authService.saveToken(response.token);
        this.router.navigate(['/articles']);
      },
      error: (err) => {
        if (err.status === 409) { 
            this.errorMessage = err.error || 'Email ou nom d\'utilisateur déjà utilisé';
          } else {
            this.errorMessage = 'Une erreur est survenue lors de l\'inscription.';
          }
      }
    });

    this.subscriptions.push(sub);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}