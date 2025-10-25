import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface OrderItem {
    productName: string;
    quantity: number;
    unitPrice: number;
}

export interface OrderResponse {
    id: number;
    status: string;
    subtotal: number;
    total: number;
    items: OrderItem[];
    paymentMethod: string;
    trackingCode: string;
}

@Injectable({ providedIn: 'root' })
export class OrderService {
    private baseUrl = `${environment.apiUrl}/orders`;

    constructor(private http: HttpClient) { }

    create(userId: number, shippingAddressId: number, paymentMethod: string): Observable<OrderResponse> {
        return this.http.post<OrderResponse>(
            `${this.baseUrl}?userId=${userId}`,
            { shippingAddressId, paymentMethod }
        );
    }

    getOrdersByUser(userId: number): Observable<OrderResponse[]> {
        return this.http.get<OrderResponse[]>(`${this.baseUrl}/user/${userId}`);
    }
}
