import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SubjectService } from '../../core/services/subject/subject.service';
import { SubscriptionService } from '../../core/services/Subscription/subscription.service';
import { SubjectResponse } from '../../shared/models/subject.model';

@Component({
  selector: 'app-theme',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './theme.component.html',
  styleUrls: ['./theme.component.css']
})
export class ThemeComponent implements OnInit {
  private subjectService = inject(SubjectService);
  private subscriptionService = inject(SubscriptionService);

  subjects: (SubjectResponse & { subscribed?: boolean })[] = [];

  ngOnInit(): void {
    this.subjectService.getSubjects().subscribe({
      next: (subjects) => {
        this.subjects = subjects;

        this.subjects.forEach((subject) => {
          this.subscriptionService.isSubscribed(subject.id).subscribe({
            next: (isSubscribed) => {
              subject.subscribed = isSubscribed;
            },
            error: () => {
              subject.subscribed = false;
            }
          });
        });
      },
      error: (err) => {
        console.error('[Erreur] Chargement des sujets :', err);
      }
    });
  }

  toggleSubscription(subject: SubjectResponse & { subscribed?: boolean }): void {
    if (subject.subscribed) {
      this.subscriptionService.unsubscribe(subject.id).subscribe({
        next: () => subject.subscribed = false,
        error: (err) =>
          console.error(`[Erreur] Désabonnement de ${subject.name} :`, err)
      });
    } else {
      this.subscriptionService.subscribe(subject.id).subscribe({
        next: () => subject.subscribed = true,
        error: (err) =>
          console.error(`[Erreur] Abonnement à ${subject.name} :`, err)
      });
    }
  }
}