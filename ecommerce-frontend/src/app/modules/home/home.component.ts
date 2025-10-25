import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../core/services/product.service';
import { CategoryService, CategoryWithProducts } from '../../core/services/category.service';
import { Banner, BannerService } from '../../core/services/banner.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  banners: Banner[] = [];
  promoProducts: any[] = [];
  monthlyOffers: any[] = [];
  categoriesWithProducts: CategoryWithProducts[] = [];
  viewedProducts: any[] = [];

  constructor(
    private bannerService: BannerService,
    private productService: ProductService,
    private categoryService: CategoryService
  ) { }

  ngOnInit(): void {

    this.bannerService.getAll().subscribe({
      next: (res) => (this.banners = res),
      error: () => (this.banners = [])
    });

    this.productService.getAll(0, 50).subscribe({
      next: (res) => {
        this.promoProducts = res.content.slice(0, 8);
        this.monthlyOffers = res.content.slice(5, 10);
        this.viewedProducts = res.content.slice(10, 15);
      },
      error: () => {
        this.promoProducts = [];
        this.monthlyOffers = [];
      }
    });

    this.categoryService.getAllWithProducts().subscribe({
      next: (res) => {
        this.categoriesWithProducts = res.map(c => ({
          ...c,
          products: c.products.slice(0, 5)
        }));
      },
      error: () => (this.categoriesWithProducts = [])
    });

  }
}