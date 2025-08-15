import { Injectable } from '@angular/core';

import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { TokenResponseDTO } from '../interfaces/TokenResponseDTO';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { JwtPayload } from '../interfaces/JwtPayload';
import { ModalService } from './modal-service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  baseApiUrl = environment.baseApiUrl;
  apiUrl = `${this.baseApiUrl}/auth`;

  private refreshInterval: any;

  private loggedInSubject = new BehaviorSubject<boolean>(
    !!localStorage.getItem('accessToken')
  );
  loggedIn$ = this.loggedInSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router,
    private modalService: ModalService,
    private toastr: ToastrService
  ) {}

  login(credencials: {
    email: string;
    password: string;
  }): Observable<TokenResponseDTO> {
    return this.http
      .post<TokenResponseDTO>(`${this.apiUrl}/login`, credencials)
      .pipe(
        tap((tokens: TokenResponseDTO) => {
          localStorage.setItem('accessToken', tokens.accessToken);
          localStorage.setItem('refreshToken', tokens.refreshToken);
          this.startTokenRefreshTimer();
          this.loggedInSubject.next(true);
        })
      );
  }

  refreshToken(): Observable<TokenResponseDTO> {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http
      .post<TokenResponseDTO>(`${this.apiUrl}/refresh`, { refreshToken })
      .pipe(
        tap((tokens: TokenResponseDTO) => {
          localStorage.setItem('accessToken', tokens.accessToken);
          localStorage.setItem('refreshToken', tokens.refreshToken);
          this.startTokenRefreshTimer();
        })
      );
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.stopTokenRefreshTimer();
    this.loggedInSubject.next(false);
    this.modalService.close();
    this.router.navigate(['/login']);
  }

  startTokenRefreshTimer() {
    const token = localStorage.getItem('accessToken');

    if (!token) return;

    const decoded = jwtDecode<JwtPayload>(token);
    const exp = decoded.exp * 1000;
    const now = Date.now();
    const delay = exp - now - 30_000;

    if (delay <= 0) {
      // token já expirou ou vai expirar em menos de 30 segundos
      this.tryRefreshOrLogout();
      return;
    }

    if (this.refreshInterval) {
      this.stopTokenRefreshTimer();
    }

    this.refreshInterval = setTimeout(() => {
      this.tryRefreshOrLogout();
    }, delay);
  }

  public tryRefreshOrLogout() {
    this.refreshToken().subscribe({
      next: () => this.toastr.info('Token atualizado', 'Sessão revalidada'),
      error: (err) => {
        if (err.status === 401) {
          this.toastr.error('Token inválido', 'Erro');
          this.logout();
        } else if (err.status === 403) {
          this.toastr.error('Acesso negado', 'Erro');
          this.logout();
        } else {
          this.toastr.error('Erro inesperado ao renovar token', 'Erro');
          this.logout();
        }
      },
    });
  }

  stopTokenRefreshTimer() {
    clearTimeout(this.refreshInterval);
  }
}
