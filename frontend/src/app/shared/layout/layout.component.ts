import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { NavbarComponent } from '../components/navbar/navbar.component';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-layout',
  imports: [RouterOutlet,NavbarComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {
  isLoggedIn = true; // tu remplaceras par l’appel à AuthService plus tard
  menuOpen = false;

  showNavbar = true;

  constructor(
    private router: Router,
    private breakpointObserver: BreakpointObserver
  ) {
    // Écoute les changements de route
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(() => {
        const isRegisterRoute = this.router.url.includes('/register');
        const isLoginRoute = this.router.url.includes('/login');
        // Vérifie si on est en mode mobile
        this.breakpointObserver.observe(['(max-width: 768px)'])
          .subscribe(result => {
            const isMobile = result.matches;
            this.showNavbar = !((isMobile && isRegisterRoute)|| (isMobile && isLoginRoute));
          });
      });
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }

  logout() {
    // Logique de déconnexion ici (ex: AuthService.logout())
    this.isLoggedIn = false;
    this.router.navigate(['/']);
  }
}
