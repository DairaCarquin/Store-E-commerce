import { Component, Input, OnInit, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { Product } from '../../../core/services/product.service';

@Component({
  selector: 'app-product-carousel',
  templateUrl: './product-carousel.component.html',
  styleUrls: ['./product-carousel.component.css']
})
export class ProductCarouselComponent implements OnInit, OnDestroy, OnChanges {
  @Input() products: Product[] = [];
  @Input() itemsPerView: number = 4;

  visibleProducts: Product[] = [];
  startIndex = 0;
  interval: any;

  ngOnInit() { }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['products'] || changes['itemsPerView']) {
      if (this.products?.length > 0) {
        this.updateVisibleProducts();
        this.startAutoSlide();
      }
    }
  }

  ngOnDestroy() {
    this.stopAutoSlide();
  }

  updateVisibleProducts() {
    if (!this.products.length) return;

    const end = this.startIndex + this.itemsPerView;

    if (end <= this.products.length) {
      this.visibleProducts = this.products.slice(this.startIndex, end);
    } else {
      this.visibleProducts = [
        ...this.products.slice(this.startIndex),
        ...this.products.slice(0, end - this.products.length)
      ];
    }
  }

  next() {
    if (!this.products?.length) return;
    this.startIndex = (this.startIndex + 1) % this.products.length;
    this.updateVisibleProducts();
  }

  prev() {
    if (!this.products?.length) return;
    this.startIndex = (this.startIndex - 1 + this.products.length) % this.products.length;
    this.updateVisibleProducts();
  }

  startAutoSlide() {
    this.stopAutoSlide();
    this.interval = setInterval(() => this.next(), 4000);
  }

  stopAutoSlide() {
    if (this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }
  }
}
