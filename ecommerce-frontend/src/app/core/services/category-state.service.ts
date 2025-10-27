import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface SelectedCategory {
    id: number;
    name: string;
}

@Injectable({ providedIn: 'root' })
export class CategoryStateService {
    private selectedCategorySubject = new BehaviorSubject<SelectedCategory | null>(null);
    selectedCategory$ = this.selectedCategorySubject.asObservable();

    setCategory(category: SelectedCategory) {
        localStorage.setItem('selectedCategory', JSON.stringify(category));
        this.selectedCategorySubject.next(category);
    }

    getCategory(): SelectedCategory | null {
        const stored = localStorage.getItem('selectedCategory');
        return stored ? JSON.parse(stored) : null;
    }

    initialize() {
        const stored = this.getCategory();
        if (stored) this.selectedCategorySubject.next(stored);
    }
}
