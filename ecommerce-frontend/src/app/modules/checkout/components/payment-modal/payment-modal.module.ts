import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { PaymentModalComponent } from './payment-modal.component';

@NgModule({
    declarations: [PaymentModalComponent],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule
    ],
    exports: [PaymentModalComponent]
})
export class PaymentModalModule { }
