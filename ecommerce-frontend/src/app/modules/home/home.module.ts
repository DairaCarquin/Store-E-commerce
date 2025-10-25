import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { SharedModule } from '../../shared/shared.module';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    SharedModule,
    MatIconModule,
    RouterModule.forChild([
      { path: '', component: HomeComponent }
    ])
  ]
})
export class HomeModule {}
