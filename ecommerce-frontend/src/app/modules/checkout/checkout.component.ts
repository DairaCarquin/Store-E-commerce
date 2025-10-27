import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartService, Cart } from '../../core/services/cart.service';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  billingForm!: FormGroup;
  cart: Cart | null = null;
  paymentMethod: string = 'card';
  showPaymentModal = false;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private cartService: CartService,
    private router: Router
  ) { }

  ngOnInit() {
    this.billingForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dni: [''],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      address: ['', Validators.required],
      country: ['Perú', Validators.required],
      region: ['', Validators.required],
      city: ['', Validators.required]
    });

    this.cartService.cart$.subscribe(cart => (this.cart = cart));
  }

  setPaymentMethod(method: string) {
    this.paymentMethod = method;
  }

  placeOrder() {
    if (this.billingForm.invalid) {
      this.billingForm.markAllAsTouched();
      return;
    }

    if (this.paymentMethod === 'card') {
      this.showPaymentModal = true;
    } else {
      this.finalizeOrder();
    }
  }

  onPaymentSuccess() {
    this.finalizeOrder();
  }

  finalizeOrder() {
    const userId = this.authService.getUserId();
    if (!userId) {
      alert('Por favor inicia sesión para finalizar tu compra.');
      this.router.navigate(['/login']);
      return;
    }

    this.cartService.getCart(userId).subscribe({
      next: (cart) => {
        this.cartService.clearCart(userId);
        alert('🛍️ Pedido registrado con éxito');
        this.router.navigate(['/']);
      },
      error: () => alert('Error al guardar el pedido')
    });

    this.showPaymentModal = false;
  }
}
