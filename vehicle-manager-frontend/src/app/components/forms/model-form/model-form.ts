import { Component, OnInit } from '@angular/core';
import { Model } from '../../../interfaces/Model';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ModelFormService } from '../../../services/form/model-form-service';
import { Brand } from '../../../interfaces/Brand';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { ModelService } from '../../../services/api/model-service';
import { BrandService } from '../../../services/api/brand-service';

@Component({
  selector: 'app-model-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './model-form.html',
  styleUrl: './model-form.css',
})
export class ModelForm implements OnInit {
  modelForm!: FormGroup;
  isVisible = false;
  isEditing = false;

  brands: Brand[] = [];

  constructor(
    private fb: FormBuilder,
    private modelFormService: ModelFormService,
    private toastr: ToastrService,
    private modelService: ModelService,
    private brandService: BrandService
  ) {}

  ngOnInit() {
    this.modelForm = this.fb.group({
      id: [null],
      name: ['', [Validators.required, Validators.maxLength(100)]],
      brand: ['', Validators.required],
    });

    this.modelFormService.isVisible$.subscribe((visible) => {
      this.isVisible = visible;
    });

    this.modelFormService.modelData$.subscribe((model) => {
      if (model) {
        this.isEditing = true;
        this.modelForm.patchValue({
          id: model.id,
          name: model.name,
          brand: model.brand.id,
        });
      } else {
        this.isEditing = false;
        this.modelForm.reset();
      }
    });

    this.brandService.brands$.subscribe((brands) => {
      this.brands = brands;
    });

    this.brandService.getAll().subscribe();
  }

  onSubmit() {
    const payload: Model = {
      id: this.modelForm.value.id,
      name: this.modelForm.value.name,
      brand: this.brands.find((b) => b.id === +this.modelForm.value.brand)!,
    };

    if (this.isEditing && payload.id) {
      if (this.modelForm.pristine) {
        this.toastr.info(
          'Você deve fazer alterações',
          'Nenhuma mudança detectada'
        );
        return;
      }
      this.updateModel(payload);
    } else {
      this.insertModel(payload);
    }
  }

  updateModel(payload: Model) {
    this.modelService.update(payload.id, payload).subscribe({
      next: () => {
        this.toastr.success('Modelo atualizado com sucesso', 'Sucesso');
        this.close();
      },
      error: (err) => {
        if (err.status === 409 && err.error.code === 'MODEL_NAME_EXISTS') {
          this.toastr.error('Nome já está sendo utilizado.', 'Erro');
        } else {
          this.toastr.error('Erro ao atualizar modelo', 'Erro');
          console.error(err);
        }
      },
    });
  }

  insertModel(payload: Model) {
    this.modelService.insert(payload).subscribe({
      next: () => {
        this.toastr.success('Modelo adicionado com sucesso', 'Sucesso');
        this.close();
      },
      error: (err) => {
        if (err.status === 409 && err.error.code === 'MODEL_NAME_EXISTS') {
          this.toastr.error('Nome já está sendo utilizado.', 'Erro');
        } else {
          this.toastr.error('Erro ao adicionar modelo', 'Erro');
          console.error(err);
        }
      },
    });
  }

  close() {
    this.modelFormService.close();
  }
}
