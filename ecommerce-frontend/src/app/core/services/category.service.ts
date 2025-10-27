import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Category {
    id: number;
    name: string;
    description: string;
    imageBase64: string;
    active: boolean;
    products?: Product[];
}

export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    stock: number;
    imageBase64: string;
    categoryId: number;
    category: string;
    active: boolean;
}

export interface CategoryWithProducts {
    id: number;
    name: string;
    description: string;
    imageBase64: string | null;
    active: boolean;
    products: Product[];
}

@Injectable({ providedIn: 'root' })
export class CategoryService {
    private baseUrl = `${environment.apiUrl}/categories`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<Category[]> {
        return this.http.get<Category[]>(this.baseUrl);
    }

    getAllWithProducts(): Observable<CategoryWithProducts[]> {
        return this.http.get<CategoryWithProducts[]>(`${this.baseUrl}/with-products`);
    }

    create(category: Partial<Category>): Observable<Category> {
        return this.http.post<Category>(this.baseUrl, category);
    }

    update(id: number, category: Partial<Category>): Observable<Category> {
        return this.http.put<Category>(`${this.baseUrl}/${id}`, category);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
}
