import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { ModalService } from './modal-service';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class ActivityMonitorService {
  private inactivityTimeLimit = 15 * 60 * 1000;
  private warningTime = 60 * 1000;
  private warningTimer: any;
  private logoutTimer: any;

  private isMonitoring = false;

  private eventHandler = () => this.resetTimer();

  constructor(
    private authService: AuthService,
    private modalService: ModalService,
    private toastr: ToastrService
  ) {
    this.authService.loggedIn$.subscribe((isLoggedIn) => {
      if (isLoggedIn) this.initMonitoring();
      else this.stopMonitoring();
    });
  }

  initMonitoring() {
    if (this.isMonitoring) return;
    this.isMonitoring = true;

    this.resetTimer();

    const events = ['keydown', 'mousedown', 'touchstart'];
    events.forEach((event) => {
      document.addEventListener(event, this.eventHandler);
    });
  }

  stopMonitoring() {
    if (!this.isMonitoring) return;
    this.isMonitoring = false;

    clearTimeout(this.warningTimer);
    clearTimeout(this.logoutTimer);

    const events = ['keydown', 'mousedown', 'touchstart'];
    events.forEach((event) => {
      document.removeEventListener(event, this.eventHandler);
    });
  }

  private resetTimer() {
    clearTimeout(this.warningTimer);
    clearTimeout(this.logoutTimer);

    this.warningTimer = setTimeout(
      () => this.showSessionWarning(),
      this.inactivityTimeLimit - this.warningTime
    );

    this.logoutTimer = setTimeout(() => {
      this.toastr.error('Fazendo logout...', 'Usuário inativo');
      this.authService.logout();
    }, this.inactivityTimeLimit);
  }

  private showSessionWarning() {
    this.modalService
      .show({
        title: 'Sessão prestes a expirar',
        message:
          'Sua sessão irá expirar em 1 minuto. Deseja continuar conectado?',
        confirmText: 'Continuar',
        cancelText: 'Sair',
      })
      .subscribe((confirmado) => {
        if (confirmado) {
          this.authService.tryRefreshOrLogout();
        } else {
          this.authService.logout();
        }
      });
  }
}
