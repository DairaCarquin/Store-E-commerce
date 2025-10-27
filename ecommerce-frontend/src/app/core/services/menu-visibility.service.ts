import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MenuVisibilityService {
    private visibleSubject = new BehaviorSubject<boolean>(false);
    visible$ = this.visibleSubject.asObservable();

    open() {
        this.visibleSubject.next(true);
        document.body.classList.add('menu-open');
    }

    close() {
        this.visibleSubject.next(false);
        document.body.classList.remove('menu-open');
    }

    toggle() {
        const current = this.visibleSubject.value;
        current ? this.close() : this.open();
    }
}
