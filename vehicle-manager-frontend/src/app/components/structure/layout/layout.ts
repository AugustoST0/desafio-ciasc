import { Component, OnInit } from '@angular/core';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../services/api/auth-service';

@Component({
  selector: 'app-layout',
  imports: [Header, Footer, RouterModule],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout implements OnInit {
  constructor(private authService: AuthService) {}

  ngOnInit() {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      this.authService.logout();
    }
  }
}
