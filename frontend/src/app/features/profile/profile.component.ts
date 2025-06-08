import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

import { AuthService } from '../../core/auth/auth.service';
import { SubscriptionService } from '../../core/services/Subscription/subscription.service';
import { SubjectService } from '../../core/services/subject/subject.service';

import { UserRequest, UserResponse } from '../../shared/models/auth.model';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private subscriptionService = inject(SubscriptionService);
  private subjectService = inject(SubjectService);

  userData?: UserResponse;

  profileForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', Validators.required],
    password: ['']
  });

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.authService.getProfile().subscribe({
      next: (data) => {
        this.userData = data;
        this.profileForm.patchValue({
          email: data.email,
          username: data.username
        });
      },
      error: (err) => {
        console.error('Erreur lors du chargement du profil :', err);
      }
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) {
      this.profileForm.markAllAsTouched();
      return;
    }

    const updateData: UserRequest = {
      email: this.profileForm.value.email ?? '',
      username: this.profileForm.value.username ?? '',
      password: this.profileForm.value.password || undefined
    };

    this.authService.updateProfile(updateData).subscribe({
      next: (updatedUser) => {
        alert('Profil mis à jour !');
        this.userData = updatedUser;
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour :', err);
      }
    });
  }

  toggleSubscription(subjectName: string): void {
    this.subjectService.getSubjectByName(subjectName).subscribe({
      next: (subject) => {
        const subjectId = subject.id;
        this.subscriptionService.unsubscribe(subjectId).subscribe({
          next: () => {
            console.log(`Désabonnement réussi de : ${subjectName}`);
            this.userData!.subscriptions = this.userData!.subscriptions.filter(
              (s) => s.name !== subjectName
            );
          },
          error: (err) => {
            console.error(`[Erreur] lors du désabonnement de ${subjectName} :`, err);
          }
        });
      },
      error: (err) => {
        console.error(`[Erreur] Sujet non trouvé : ${subjectName}`, err);
      }
    });
  }
}