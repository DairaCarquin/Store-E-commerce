import { Component, OnInit } from '@angular/core';
import { CategoryService } from '../../../core/services/category.service';
import { LogoService, Logo } from '../../../core/services/logo.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  categories: any[] = [];
  repeatArray = Array(10);
  logo: Logo | null = null;

  constructor(
    private categoryService: CategoryService,
    private logoService: LogoService
  ) {}

  ngOnInit() {
    this.categoryService.getAll().subscribe({
      next: (data) => (this.categories = data),
      error: () => (this.categories = [])
    });

    this.logoService.getAll().subscribe({
      next: (res) => {
        this.logo = res.find(l => l.active) || null;
      },
      error: () => (this.logo = null)
    });
  }
}
