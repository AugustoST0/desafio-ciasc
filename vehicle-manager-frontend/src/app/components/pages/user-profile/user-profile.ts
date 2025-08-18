import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { User } from '../../../interfaces/User';
import { UserService } from '../../../services/api/user-service';
import { ModalService } from '../../../services/modal-service';
import { AuthService } from '../../../services/api/auth-service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-profile',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile implements OnInit {
  profileForm!: FormGroup;
  user!: User | null;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private modalService: ModalService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.profileForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
    });

    this.authService.currentUser$.subscribe((user) => {
      if (user) {
        this.user = user;
        this.profileForm.patchValue({
          name: user.name,
          email: user.email,
        });
      }
    });
  }

  onSubmit() {
    if (this.profileForm.pristine) {
      this.toastr.info(
        'Você deve fazer alterações',
        'Nenhuma mudança detectada'
      );
      return;
    }

    const password = this.profileForm.get('password')?.value;
    const confirmPassword = this.profileForm.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      this.toastr.error('Senhas não conferem', 'Erro');
      return;
    }

    const updatedUser: User = {
      name: this.profileForm.get('name')?.value || this.user!.name,
      email: this.profileForm.get('email')?.value || this.user!.email,
      password: password || null,
      admin: this.user!.admin
    };

    this.userService.update(this.user!.id!, updatedUser).subscribe({
      next: (response) => {
        this.user = response.user;
        this.authService.setTokens(response.accessToken, response.refreshToken);
        this.authService.loadUserFromToken();
        this.profileForm.get('password')?.reset();
        this.profileForm.get('confirmPassword')?.reset();
        this.profileForm.markAsPristine();
        this.profileForm.markAsUntouched();
        this.toastr.success('Usuário atualizado com sucesso', 'Sucesso');
      },
      error: (err) => {
        if (err.status === 409 && err.error.code === 'EMAIL_ALREADY_EXISTS') {
          this.toastr.error('E-mail já está sendo utilizado.', 'Erro');
        } else {
          this.toastr.error('Erro ao atualizar usuário.', 'Erro');
        }
      },
    });
  }

  confirmDeleteAccount() {
    this.modalService
      .show({
        title: 'Confirmação de remoção',
        message: 'Esta ação é irreversível. Deseja deletar esta conta?',
        confirmText: 'Deletar',
        cancelText: 'Cancelar',
      })
      .subscribe((confirmado) => {
        if (confirmado) {
          this.deleteAccount();
        } else {
          this.modalService.close();
        }
      });
  }

  deleteAccount() {
    this.userService.delete(this.user!.id!).subscribe({
      next: () => {
        this.toastr.success('Conta deletada com sucesso', 'Sucesso');
        this.authService.logout();
      },
      error: (err) => {
        this.toastr.error('Erro ao deletar conta', 'Erro');
        console.error(err);
      },
    });
  }
}
