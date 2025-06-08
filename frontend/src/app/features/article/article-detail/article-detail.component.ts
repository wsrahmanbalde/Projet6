import { Component, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../core/services/post/post.service';
import { PostResponse } from '../../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { CommentService } from '../../../core/services/comment/comment.service';
import { CommentRequest, CommentResponse } from '../../../shared/models/comment.model';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-article-detail',
  imports: [CommonModule,FormsModule],
  templateUrl: './article-detail.component.html',
  styleUrl: './article-detail.component.css'
})
export class ArticleDetailComponent {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private postService = inject(PostService);
  private commentService = inject(CommentService);

  post!: PostResponse;
  comments: CommentResponse[] = [];
  newComment: string = '';

  private subscriptions: Subscription[] = [];

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (isNaN(id)) {
      console.error("ID d'article invalide");
      this.router.navigate(['/articles']);
      return;
    }

    this.loadPost(id);
    this.loadComments(id);
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  goBack(): void {
    this.router.navigate(['/articles']);
  }

  loadPost(id: number): void {
    const sub = this.postService.getById(id).subscribe({
      next: (data) => this.post = data,
      error: (err) => console.error("Erreur lors du chargement de l'article :", err)
    });
    this.subscriptions.push(sub);
  }

  loadComments(postId: number): void {
    const sub = this.commentService.getComments(postId).subscribe({
      next: (data) => this.comments = data,
      error: (err) => console.error("Erreur lors du chargement des commentaires :", err)
    });
    this.subscriptions.push(sub);
  }

  submitComment(): void {
    const content = this.newComment.trim();

    if (!content || !this.post?.id) return;
    
    const request: CommentRequest = {
      content,
      postId: this.post.id
    };

    const sub = this.commentService.addComment(request).subscribe({
      next: (newCom) => {
        this.comments.push(newCom);
        this.newComment = '';
      },
      error: (err) => {
        console.error("Erreur lors de l'ajout du commentaire :", err);
      }
    });

    this.subscriptions.push(sub);
  }
}
