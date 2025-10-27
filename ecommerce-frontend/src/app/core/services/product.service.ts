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
  imageBase64 : string;
  category: string;
  categoryId?: number;
  active: boolean;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class ProductService {
  private baseUrl = `${environment.apiUrl}/products`;

  constructor(private http: HttpClient) { }

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

  getByCategory(categoryId: number, page = 0, size = 12): Observable<PageResponse<Product>> {
    return this.http.get<PageResponse<Product>>(`${this.baseUrl}/category/${categoryId}?page=${page}&size=${size}`);
  }

  getBySearch(query: string, page: number, size: number) {
    const price = parseFloat(query);
    const isPrice = !isNaN(price);

    let url = `${this.baseUrl}/search?page=${page}&size=${size}`;
    if (isPrice) url += `&price=${price}`;
    else url += `&name=${encodeURIComponent(query)}`;

    return this.http.get<PageResponse<Product>>(url);
  }


}
