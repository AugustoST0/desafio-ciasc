import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import {
  Validators,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { UserService } from '../../../services/api/user-service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../services/api/auth-service';
import { take } from 'rxjs';

@Component({
  selector: 'app-cadastro',
  imports: [RouterModule, ReactiveFormsModule],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css',
})
export class Cadastro implements OnInit {
  registerForm!: FormGroup;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.authService.currentUser$
      .pipe(take(1))
      .subscribe((currentUserExists) => {
        if (currentUserExists) {
          this.router.navigate(['/dashboard']);
        }
      });
  }

  submit() {
    if (this.registerForm.invalid) {
      this.toastr.error('Preencha todos os campos corretamente.', 'Erro');
      this.registerForm.markAllAsTouched();
      return;
    }

    const password = this.registerForm.get('password')?.value;
    const confirmPassword = this.registerForm.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      this.toastr.error('Senham não conferem.', 'Erro');
      return;
    }

    const userPayload = {
      name: this.registerForm.get('name')?.value,
      email: this.registerForm.get('email')?.value,
      password: password,
    };

    this.userService.register(userPayload).subscribe({
      next: () => {
        this.toastr.success('Usuário cadastrado com sucesso.', 'Sucesso');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        if (err.status === 409 && err.error.code === 'EMAIL_ALREADY_EXISTS') {
          this.toastr.error('E-mail já está sendo utilizado.', 'Erro');
        } else {
          this.toastr.error('Erro inesperado.', 'Erro');
        }
      },
    });
  }
}
