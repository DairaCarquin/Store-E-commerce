import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  imageUrl: string;
  category: string;
  active: boolean;
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private baseUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) {}

  getAll(page: number = 0, size: number = 20): Observable<{ content: Product[] }> {
    return this.http.get<{ content: Product[] }>(`${this.baseUrl}?page=${page}&size=${size}`);
  }

  getById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`);
  }

  create(product: Partial<Product>): Observable<Product> {
    return this.http.post<Product>(this.baseUrl, product);
  }

  update(id: number, product: Partial<Product>): Observable<Product> {
    return this.http.put<Product>(`${this.baseUrl}/${id}`, product);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
