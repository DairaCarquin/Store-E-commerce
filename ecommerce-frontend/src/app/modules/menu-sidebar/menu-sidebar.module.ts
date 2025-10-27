import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuSidebarComponent } from './menu-sidebar.component';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
    declarations: [MenuSidebarComponent],
    imports: [CommonModule, MatIconModule, FormsModule, SharedModule],
    exports: [MenuSidebarComponent]
})
export class MenuSidebarModule { }
