import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payment-modal',
  templateUrl: './payment-modal.component.html',
  styleUrls: ['./payment-modal.component.css']
})
export class PaymentModalComponent {
  @Output() success = new EventEmitter<void>();
  @Output() closeModal = new EventEmitter<void>();

  cardForm: FormGroup;
  processing = false;
  successAnimation = false;

  constructor(private fb: FormBuilder, private router: Router) {
    this.cardForm = this.fb.group({
      cardNumber: ['', [Validators.required, Validators.pattern(/^\d{16}$/)]],
      expiry: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])\/\d{2}$/)]],
      cvv: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]],
      cardHolder: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  isInvalid(control: string): boolean {
    const field = this.cardForm.get(control);
    return !!field && field.invalid && field.touched;
  }

  formatCardNumber(event: any) {
    let value = event.target.value.replace(/\D/g, '').substring(0, 16);
    event.target.value = value.replace(/(.{4})/g, '$1 ').trim();
    this.cardForm.get('cardNumber')?.setValue(value, { emitEvent: false });
  }

  formatExpiry(event: any) {
    let value = event.target.value.replace(/\D/g, '').substring(0, 4);
    if (value.length >= 3) value = value.replace(/(\d{2})(\d{1,2})/, '$1/$2');
    event.target.value = value;
    this.cardForm.get('expiry')?.setValue(value, { emitEvent: false });
  }

  pay() {
    if (this.cardForm.invalid) {
      this.cardForm.markAllAsTouched();
      return;
    }

    this.processing = true;
    setTimeout(() => {
      this.processing = false;
      this.successAnimation = true;

      setTimeout(() => {
        this.success.emit();
        this.close();
        this.router.navigate(['/']);
      }, 1800);
    }, 1200);
  }

  close() {
    this.closeModal.emit();
  }
}
