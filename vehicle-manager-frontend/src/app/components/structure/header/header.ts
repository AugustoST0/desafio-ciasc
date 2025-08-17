import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../../../services/api/user-service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/api/auth-service';
import { User } from '../../../interfaces/User';

@Component({
  selector: 'app-header',
  imports: [RouterModule, CommonModule, MatIconModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {
  user!: User | null;

  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe((user) => {
      if (user) {
        this.user = user;
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
