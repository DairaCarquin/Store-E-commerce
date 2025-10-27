import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { CartService } from './cart.service';

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  phone?: string;
  dni?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  email: string;
  userId: number;
  role?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;
  private TOKEN_KEY = 'authToken';
  private USER_KEY = 'userData';

  constructor(private http: HttpClient, private cartService: CartService) { }
  register(req: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, req);
  }

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, req);
  }

  saveSession(res: AuthResponse) {
    localStorage.setItem('authToken', res.token);
    localStorage.setItem('userData', JSON.stringify({
      userId: res.userId,
      email: res.email,
      role: res.role || 'USER'
    }));
  }
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  getUserId(): number | null {
    const data = localStorage.getItem(this.USER_KEY);
    return data ? JSON.parse(data).userId : null;
  }
}
