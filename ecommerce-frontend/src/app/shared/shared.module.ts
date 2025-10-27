import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { CarouselComponent } from './components/carousel/carousel.component';
import { ProductCardComponent } from './components/product-card/product-card.component';
import { ProductCarouselComponent } from './components/product-carousel/product-carousel.component';
import { Base64ImagePipe } from './pipes/base64-image.pipe';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    CarouselComponent,
    ProductCardComponent,
    ProductCarouselComponent,
    Base64ImagePipe
  ],
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    CarouselComponent,
    ProductCardComponent,
    ProductCarouselComponent,
    Base64ImagePipe,
    MatIconModule
  ]
})
export class SharedModule { }
