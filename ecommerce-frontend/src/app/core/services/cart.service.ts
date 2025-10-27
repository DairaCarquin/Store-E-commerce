import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface CartItem {
    productId: number;
    productName: string;
    description?: string;
    imageBase64?: string;
    quantity: number;
    unitPrice: number;
}

export interface Cart {
    items: CartItem[];
    subtotal: number;
    discount: number;
    total: number;
}

@Injectable({ providedIn: 'root' })
export class CartService {
    private baseUrl = `${environment.apiUrl}/cart`;
    private cartSubject = new BehaviorSubject<Cart | null>(null);
    cart$ = this.cartSubject.asObservable();
    private localStorageKey = 'localCart';

    constructor(private http: HttpClient) {
        const stored = localStorage.getItem(this.localStorageKey);
        if (stored) this.cartSubject.next(JSON.parse(stored));
    }

    getCart(userId: number): Observable<Cart> {
        const obs = this.http.get<Cart>(`${this.baseUrl}/${userId}`);
        obs.subscribe(cart => {
            this.cartSubject.next(cart);
            this.saveLocal(cart);
        });
        return obs;
    }

    addItem(
        userId: number,
        productId: number,
        quantity: number,
        name: string,
        price: number,
        description: string,
        imageBase64: string
    ): Observable<Cart> {
        const body = { productId, quantity, name, price, description, imageBase64 };
        const obs = this.http.post<Cart>(`${this.baseUrl}/${userId}/items`, body);
        obs.subscribe(cart => {
            this.cartSubject.next(cart);
            this.saveLocal(cart);
        });
        return obs;
    }

    updateItem(userId: number, productId: number, quantity: number): void {
        const obs = this.http.put<Cart>(`${this.baseUrl}/${userId}/items/${productId}`, { quantity });
        obs.subscribe({
            next: (cart) => {
                this.cartSubject.next(cart);
                this.saveLocal(cart);
            },
            error: (err) => console.error('Error al actualizar cantidad', err)
        });
    }

    removeItem(userId: number, productId: number): void {
        const obs = this.http.delete<Cart>(`${this.baseUrl}/${userId}/items/${productId}`);
        obs.subscribe({
            next: (cart) => {
                this.cartSubject.next(cart);
                this.saveLocal(cart);
            },
            error: (err) => console.error('Error al eliminar producto', err)
        });
    }

    clearCart(userId: number): void {
        const obs = this.http.delete<Cart>(`${this.baseUrl}/${userId}`);
        obs.subscribe(cart => {
            this.cartSubject.next(cart);
            this.saveLocal(cart);
        });
    }

    private saveLocal(cart: Cart) {
        localStorage.setItem(this.localStorageKey, JSON.stringify(cart));
    }
}
