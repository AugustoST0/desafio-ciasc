import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Brand } from '../../../interfaces/Brand';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { BrandFormService } from '../../../services/form/brand-form-service';
import { BrandService } from '../../../services/api/brand-service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-brand-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './brand-form.html',
  styleUrl: './brand-form.css',
})
export class BrandForm implements OnInit {
  brandForm!: FormGroup;
  isVisible = false;
  isEditing = false;

  constructor(
    private fb: FormBuilder,
    private brandFormService: BrandFormService,
    private brandService: BrandService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.brandForm = this.fb.group({
      id: [null],
      name: ['', Validators.required],
    });

    this.brandFormService.isVisible$.subscribe((visible) => {
      this.isVisible = visible;
    });

    this.brandFormService.brandData$.subscribe((brand) => {
      if (brand) {
        this.isEditing = true;
        this.brandForm.patchValue(brand);
      } else {
        this.isEditing = false;
        this.brandForm.reset();
      }
    });
  }

  onSubmit() {
    const brand: Brand = this.brandForm.value;

    if (this.isEditing && brand.id) {
      if (this.brandForm.pristine) {
        this.toastr.info(
          'Você deve fazer alterações',
          'Nenhuma mudança detectada'
        );
        return;
      }

      // update
      this.brandService.update(brand.id, brand).subscribe({
        next: () => {
          this.toastr.success('Marca atualizada com sucesso', 'Sucesso');
          this.close();
        },
        error: (err) => {
          this.toastr.error('Erro ao atualizar marca', 'Erro');
          console.error(err);
        },
      });
    } else {
      // insert
      this.brandService.insert(brand).subscribe({
        next: () => {
          this.toastr.success('Marca adicionada com sucesso', 'Sucesso');
          this.close();
        },
        error: (err) => {
          this.toastr.error('Erro ao adicionar marca', 'Erro');
          console.error(err);
        },
      });
    }
  }

  close() {
    this.brandFormService.close();
  }
}
