import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CartItem {
    productId: number;
    name: string;
    quantity: number;
    price: number;
}

export interface CartResponse {
    items: CartItem[];
    subtotal: number;
    discount: number;
    total: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
    private baseUrl = `${environment.apiUrl}/cart`;

    constructor(private http: HttpClient) { }

    getCart(userId: number): Observable<CartResponse> {
        return this.http.get<CartResponse>(`${this.baseUrl}/${userId}`);
    }

    addItem(userId: number, productId: number, quantity: number): Observable<CartResponse> {
        return this.http.post<CartResponse>(`${this.baseUrl}/${userId}/items`, { productId, quantity });
    }

    updateItem(userId: number, productId: number, quantity: number): Observable<CartResponse> {
        return this.http.put<CartResponse>(`${this.baseUrl}/${userId}/items/${productId}`, { quantity });
    }

    removeItem(userId: number, productId: number): Observable<CartResponse> {
        return this.http.delete<CartResponse>(`${this.baseUrl}/${userId}/items/${productId}`);
    }

    clearCart(userId: number): Observable<CartResponse> {
        return this.http.delete<CartResponse>(`${this.baseUrl}/${userId}`);
    }
}
