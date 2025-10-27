import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthStateService } from '../../core/services/auth-state.service'; 

interface User {
  fullName: string;
  email: string;
  phone?: string;
  dni?: string;
}

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  user: User | null = null;
  selectedSection: string = 'info';

  constructor(
    private router: Router,
    private authState: AuthStateService
  ) {}

  ngOnInit() {
    const storedUser = localStorage.getItem('userData');
    this.user = storedUser ? JSON.parse(storedUser) : null;
  }

  selectSection(section: string) {
    this.selectedSection = section;
  }

  logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userData');
    this.authState.setLoggedIn(false);
    this.router.navigate(['/']);
  }
}
