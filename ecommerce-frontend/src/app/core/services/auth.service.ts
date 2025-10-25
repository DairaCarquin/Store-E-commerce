import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

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
}

@Injectable({ providedIn: 'root' })
export class AuthService {
    private baseUrl = `${environment.apiUrl}/auth`;

    constructor(private http: HttpClient) { }

    register(req: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.baseUrl}/register`, req);
    }

    login(req: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.baseUrl}/login`, req);
    }

    logout() {
        localStorage.removeItem('token');
    }

    saveToken(token: string) {
        localStorage.setItem('token', token);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    isAuthenticated(): boolean {
        return !!localStorage.getItem('token');
    }
}
