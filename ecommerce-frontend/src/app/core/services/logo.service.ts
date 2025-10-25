import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Logo {
    id: number;
    name: string;
    image: string;
    active: boolean;
}

@Injectable({ providedIn: 'root' })
export class LogoService {
    private baseUrl = `${environment.apiUrl}/logos`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<Logo[]> {
        return this.http.get<Logo[]>(this.baseUrl);
    }
}
