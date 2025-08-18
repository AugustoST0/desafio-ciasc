import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  Validators,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { UserService } from '../../../services/api/user-service';
import { ToastrService } from 'ngx-toastr';
import { User } from '../../../interfaces/User';

@Component({
  selector: 'app-cadastro',
  imports: [ReactiveFormsModule],
  templateUrl: './cadastro.html',
  styleUrls: ['./cadastro.css'],
})
export class Cadastro implements OnInit {
  registerForm!: FormGroup;

  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
      admin: [false],
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      this.toastr.error('Preencha todos os campos corretamente.', 'Erro');
      this.registerForm.markAllAsTouched();
      return;
    }

    const password = this.registerForm.get('password')?.value;
    const confirmPassword = this.registerForm.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      this.toastr.error('Senhas não conferem.', 'Erro');
      return;
    }

    const payload: User = {
      name: this.registerForm.get('name')?.value,
      email: this.registerForm.get('email')?.value,
      password: password,
      admin: this.registerForm.get('admin')?.value,
    };

    console.log(payload);

    this.userService.register(payload).subscribe({
      next: () => {
        this.toastr.success('Usuário cadastrado com sucesso.', 'Sucesso');
      },
      error: (err) => {
        if (err.status === 409 && err.error.code === 'EMAIL_ALREADY_EXISTS') {
          this.toastr.error('E-mail já está sendo utilizado.', 'Erro');
        } else {
          this.toastr.error('Erro ao registrar usuário.', 'Erro');
        }
        console.error(err);
      },
    });
  }
}
