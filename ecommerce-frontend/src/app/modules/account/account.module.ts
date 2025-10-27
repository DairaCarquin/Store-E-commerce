import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { AccountComponent } from './account.component';

@NgModule({
    declarations: [AccountComponent],
    imports: [
        CommonModule,
        FormsModule,
        MatIconModule,
        RouterModule.forChild([
            { path: '', component: AccountComponent }
        ])
    ]
})
export class AccountModule { }
