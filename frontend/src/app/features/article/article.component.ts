import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { PostService } from '../../core/services/post/post.service';
import { PostResponse } from '../../shared/models/post.model';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-article',
  standalone: true,
  imports: [RouterLink, CommonModule],
  templateUrl: './article.component.html',
  styleUrl: './article.component.css'
})
export class ArticleComponent implements OnInit, OnDestroy {
  private postService = inject(PostService);
  private router = inject(Router);

  posts: PostResponse[] = [];
  isAsc: boolean = true;
  private subscriptions: Subscription[] = [];

  toggleSort(): void {
    this.isAsc = !this.isAsc;
    const sortOrder = this.isAsc ? 'asc' : 'desc';
    this.loadPosts(sortOrder);
  }

  loadPosts(sortOrder: string): void {
    const sub = this.postService.getAll(sortOrder).subscribe({
      next: (data) => {
        this.posts = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des posts:', err);
      },
    });
    this.subscriptions.push(sub);
  }

  goToPostDetail(postId: number): void {
    this.router.navigate(['/articles', postId]);
  }

  ngOnInit(): void {
    this.loadPosts('asc');
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }
}