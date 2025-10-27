import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartSidebarComponent } from './cart-sidebar.component';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [CartSidebarComponent],
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    SharedModule
  ],
  exports: [CartSidebarComponent]
})
export class CartSidebarModule { }
