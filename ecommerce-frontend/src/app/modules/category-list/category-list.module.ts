import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { SharedModule } from '../../shared/shared.module';
import { CategoryListComponent } from './category-list.component';

@NgModule({
  declarations: [CategoryListComponent],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    MatIconModule,
    RouterModule.forChild([
      { path: '', component: CategoryListComponent }
    ])
  ]
})
export class CategoryListModule { }
