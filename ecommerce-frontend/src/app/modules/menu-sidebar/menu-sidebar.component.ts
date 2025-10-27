import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuVisibilityService } from '../../core/services/menu-visibility.service';
import { CategoryService } from '../../core/services/category.service';
import { AuthStateService } from '../../core/services/auth-state.service';
import { LogoService, Logo } from '../../core/services/logo.service';

@Component({
  selector: 'app-menu-sidebar',
  templateUrl: './menu-sidebar.component.html',
  styleUrls: ['./menu-sidebar.component.css']
})
export class MenuSidebarComponent implements OnInit {
  visible = false;
  categories: any[] = [];
  isLoggedIn = false;
  user: any = null;
  logo: Logo | null = null;

  constructor(
    private menuVisibility: MenuVisibilityService,
    private router: Router,
    private categoryService: CategoryService,
    private authState: AuthStateService,
    private logoService: LogoService 
  ) {}

  ngOnInit() {
    this.menuVisibility.visible$.subscribe(show => (this.visible = show));

    this.categoryService.getAll().subscribe({
      next: (data) => (this.categories = data),
      error: () => (this.categories = [])
    });

    this.logoService.getAll().subscribe({
      next: (res) => (this.logo = res.find(l => l.active) || null),
      error: () => (this.logo = null)
    });
    this.authState.loggedIn$.subscribe(status => {
      this.isLoggedIn = status;
      const storedUser = localStorage.getItem('userData');
      this.user = storedUser ? JSON.parse(storedUser) : null;
    });
  }

  toggle() {
    this.menuVisibility.toggle();
  }

  goTo(path: string) {
    this.menuVisibility.close();
    this.router.navigate([path]);
  }

  selectCategory(event: Event) {
    const select = event.target as HTMLSelectElement;
    const catId = select.value;
    const cat = this.categories.find(c => c.id == catId);
    if (cat) {
      this.menuVisibility.close();
      this.router.navigate(['/categories'], { queryParams: { id: cat.id } });
    }
  }

  logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userData');
    this.authState.setLoggedIn(false);
    this.menuVisibility.close();
    this.router.navigate(['/']);
  }
}
