import { Component, Input } from '@angular/core';
import { CartService } from '../../../core/services/cart.service';

@Component({
  selector: 'app-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.css']
})
export class ProductCardComponent {
  @Input() product: any;

  constructor(private cartService: CartService) {}

  addToCart() {
    this.cartService.addItem(1, this.product.id, 1, this.product.name, this.product.price, '', this.product.imageBase64).subscribe(() => {
      alert(`"${this.product.name}" agregado al carrito`);
    });
  }
}
