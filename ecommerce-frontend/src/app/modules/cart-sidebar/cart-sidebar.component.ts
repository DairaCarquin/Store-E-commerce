import { Component, OnInit } from '@angular/core';
import { CartService, Cart } from '../../core/services/cart.service';
import { CartVisibilityService } from '../../core/services/cart-visibility.service';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-cart-sidebar',
  templateUrl: './cart-sidebar.component.html',
  styleUrls: ['./cart-sidebar.component.css']
})
export class CartSidebarComponent implements OnInit {
  cart: Cart | null = null;
  visible = false;
  userId: number | null = null;

  constructor(
    private cartService: CartService,
    private cartVisibility: CartVisibilityService,
    private router: Router,
    private authService: AuthService
  ) {
    this.cartService.cart$.subscribe(c => (this.cart = c));
  }

  ngOnInit() {
    this.userId = this.authService.getUserId();

    this.cartVisibility.visibility$.subscribe(show => {
      this.visible = show;
      document.body.classList.toggle('cart-open', show);
    });

    if (this.userId) {
      this.cartService.getCart(this.userId).subscribe();
    }

    window.addEventListener('storage', () => {
      const userId = this.authService.getUserId();
      if (userId) {
        this.cartService.getCart(userId).subscribe();
      }
    });
  }

  toggle() {
    this.cartVisibility.toggle();
  }

  remove(productId: number) {
    if (this.userId !== null) {
      this.cartService.removeItem(this.userId, productId);
    } else {
      alert('⚠️ Debes iniciar sesión para modificar el carrito.');
      this.router.navigate(['/login']);
    }
  }

  update(productId: number, qty: number) {
    if (this.userId !== null) {
      this.cartService.updateItem(this.userId, productId, qty);
    } else {
      alert('⚠️ Debes iniciar sesión para modificar el carrito.');
      this.router.navigate(['/login']);
    }
  }

  clear() {
    if (this.userId !== null) {
      this.cartService.clearCart(this.userId);
    } else {
      alert('⚠️ Debes iniciar sesión para vaciar tu carrito.');
      this.router.navigate(['/login']);
    }
  }

  goToCheckout() {
    this.cartVisibility.close();
    const token = this.authService.getToken();
    if (token) {
      this.router.navigate(['/checkout']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}
