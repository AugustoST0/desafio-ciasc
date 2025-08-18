import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { Observable, map } from 'rxjs';
import { AuthService } from '../api/auth-service';

@Injectable({
  providedIn: 'root',
})
export class AdminGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean | UrlTree> | boolean | UrlTree {
    return this.authService.currentUser$.pipe(
      map((user) => {
        if (user && user.admin) {
          return true;
        } else {
          return this.router.createUrlTree(['/dashboard']);
        }
      })
    );
  }
}
