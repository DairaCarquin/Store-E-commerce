import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface Banner {
    id: number;
    title: string;
    description: string;
    buttonText: string;
    buttonLink: string;
    imageBase64: string;
    active: boolean;
}

@Injectable({ providedIn: 'root' })
export class BannerService {
    private baseUrl = `${environment.apiUrl}/banners`;

    constructor(private http: HttpClient) { }

    getAll(): Observable<Banner[]> {
        return this.http.get<Banner[]>(this.baseUrl);
    }
}
