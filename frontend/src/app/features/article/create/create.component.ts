import { ChangeDetectionStrategy, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { SubjectResponse } from '../../../shared/models/subject.model';
import { SubjectService } from '../../../core/services/subject/subject.service';
import { PostService } from '../../../core/services/post/post.service';
import { PostRequest } from '../../../shared/models/post.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './create.component.html',
  styleUrl: './create.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateComponent implements OnInit, OnDestroy {
  articleForm!: FormGroup;
  subjects: SubjectResponse[] = [];

  private postService = inject(PostService);
  private subjectService = inject(SubjectService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  errorMessage: string = '';
  private subscriptions: Subscription[] = [];

  ngOnInit(): void {
    // Initialisation du formulaire
    this.articleForm = this.fb.group({
      subjectId: ['', Validators.required],
      title: ['', Validators.required],
      content: ['', Validators.required]
    });

    // Récupérer la liste des sujets via le service
    const sub = this.subjectService.getSubjects().subscribe({
      next: (data) => {
        this.subjects = [...data];
      },
      error: (err) => {
        console.error('Erreur de chargement des sujets :', err);
      }
    });

    this.subscriptions.push(sub);
  }

  onSubmit(): void {
    if (this.articleForm.valid) {
      const postData: PostRequest = this.articleForm.value as PostRequest;
      const sub = this.postService.create(postData).subscribe({
        next: () => {
          this.router.navigate(['/articles']);
        },
        error: (error) => {
          console.log('Formulaire envoyé :', postData);
          console.error('Erreur lors de la création de l\'article :', error);
          this.errorMessage = 'Échec de la création de l\'article. Veuillez réessayer.';
        }
      });
      this.subscriptions.push(sub);
    } else {
      console.log('Form is invalid');
      this.errorMessage = 'Tous les champs sont requis.';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}