import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../../core/services/category.service';
import { LogoService, Logo } from '../../../core/services/logo.service';
import { Router } from '@angular/router';
import { CategoryStateService } from '../../../core/services/category-state.service';
import { CartVisibilityService } from '../../../core/services/cart-visibility.service';
import { AuthStateService } from '../../../core/services/auth-state.service';
import { MenuVisibilityService } from '../../../core/services/menu-visibility.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  categories: any[] = [];
  repeatArray = Array(10);
  logo: Logo | null = null;
  isLoggedIn = false;

  constructor(
    private categoryService: CategoryService,
    private logoService: LogoService,
    private router: Router,
    private categoryState: CategoryStateService,
    private cartVisibility: CartVisibilityService,
    private authState: AuthStateService,
    private menuVisibility: MenuVisibilityService
  ) { }

  ngOnInit() {
    this.authState.loggedIn$.subscribe(status => (this.isLoggedIn = status));

    this.categoryService.getAll().subscribe({
      next: (data) => {
        this.categories = data;
        const saved = this.categoryState.getCategory();
        if (!saved && data.length > 0) {
          const defaultCat = { id: data[0].id, name: data[0].name };
          this.categoryState.setCategory(defaultCat);
        }
      },
      error: () => (this.categories = [])
    });

    this.logoService.getAll().subscribe({
      next: (res) => (this.logo = res.find(l => l.active) || null),
      error: () => (this.logo = null)
    });
  }

  openCart() {
    this.cartVisibility.open();
  }

  goHome() {
    this.router.navigate(['/']);
  }

  logout() {
    localStorage.removeItem('authToken');
    this.authState.setLoggedIn(false);
    this.router.navigate(['/']);
  }

  selectCategory(cat: any) {
    this.categoryState.setCategory({ id: cat.id, name: cat.name });
    this.router.navigate(['/categories'], { queryParams: { id: cat.id } });
  }

  goToAccount() {
    this.router.navigate(['/account']);
  }

  openMenu() {
    this.menuVisibility.open();
  }
}
