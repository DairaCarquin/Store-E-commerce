import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { CheckoutComponent } from './checkout.component';
import { SharedModule } from '../../shared/shared.module';
import { PaymentModalModule } from './components/payment-modal/payment-modal.module';

const routes: Routes = [
    { path: '', component: CheckoutComponent }
];

@NgModule({
    declarations: [CheckoutComponent],
    imports: [
        CommonModule, 
        FormsModule, 
        SharedModule,
        PaymentModalModule,
        ReactiveFormsModule, 
        RouterModule.forChild(routes)]
})
export class CheckoutModule { }
