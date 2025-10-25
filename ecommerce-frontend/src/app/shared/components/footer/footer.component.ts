import { Component } from '@angular/core';
import { Logo, LogoService } from '../../../core/services/logo.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {
  currentYear: number = new Date().getFullYear();
  logo: Logo | null = null;

  constructor(
    private logoService: LogoService
  ) { }
  ngOnInit() {

    this.logoService.getAll().subscribe({
      next: (res) => {
        this.logo = res.find(l => l.active) || null;
      },
      error: () => (this.logo = null)
    });
  }
}