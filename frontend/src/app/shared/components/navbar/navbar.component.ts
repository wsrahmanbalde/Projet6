import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, MatIconModule,RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  menuOpen = false;
  isLoggedIn = this.authService.isLoggedIn;

  toggleMenu(): void  {
    this.menuOpen = !this.menuOpen;
  }

  closeMenu(): void  {
  this.menuOpen = false;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/home']);
  }
}
