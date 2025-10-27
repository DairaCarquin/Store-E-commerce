import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CartVisibilityService {
    private visibilitySubject = new BehaviorSubject<boolean>(false);
    visibility$ = this.visibilitySubject.asObservable();

    open() {
        this.visibilitySubject.next(true);
    }

    close() {
        this.visibilitySubject.next(false);
    }

    toggle() {
        this.visibilitySubject.next(!this.visibilitySubject.value);
    }
}
