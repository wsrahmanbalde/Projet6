import { Routes } from '@angular/router';
import { HomeComponent } from './shared/components/home/home.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { LayoutComponent } from './shared/layout/layout.component';
import { authGuard } from './core/auth/auth.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    loadComponent: () =>
      import('./shared/components/redirect/redirect.component').then(m => m.RedirectComponent)
  },
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'home', component: HomeComponent },
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      {
        path: 'profiles',
        canActivate: [authGuard],
        loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent),
      },
      {
        path: 'articles',
        canActivate: [authGuard],
        loadComponent: () => import('./features/article/article.component').then(m => m.ArticleComponent),
      },
      {
        path: 'themes',
        canActivate: [authGuard],
        loadComponent: () => import('./features/theme/theme.component').then(m => m.ThemeComponent)
      },
      {
        path: 'articles/create',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/article/create/create.component').then(m => m.CreateComponent)
      },
      {
        path: 'articles/:id',
        canActivate: [authGuard],
        loadComponent: () =>
          import('./features/article/article-detail/article-detail.component').then(m => m.ArticleDetailComponent)
      },
      { path: '**', loadComponent: () => import('./shared/components/not-found/not-found.component').then(m => m.NotFoundComponent) }
    ]
  }
];
