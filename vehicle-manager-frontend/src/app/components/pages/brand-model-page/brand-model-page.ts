import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { Brand } from '../../../interfaces/Brand';
import { Model } from '../../../interfaces/Model';
import { BrandService } from '../../../services/api/brand-service';
import { ModelService } from '../../../services/api/model-service';
import { ModalService } from '../../../services/modal-service';
import { BrandFormService } from '../../../services/form/brand-form-service';
import { ModelFormService } from '../../../services/form/model-form-service';

@Component({
  selector: 'app-brand-model-page',
  imports: [CommonModule],
  templateUrl: './brand-model-page.html',
  styleUrls: ['./brand-model-page.css'],
})
export class BrandModelPage implements OnInit {
  brands: Brand[] = [];
  models: Model[] = [];

  constructor(
    private brandService: BrandService,
    private modelService: ModelService,
    private brandFormService: BrandFormService,
    private modelFormService: ModelFormService,
    private toastr: ToastrService,
    private modalService: ModalService
  ) {}

  ngOnInit() {
    this.brandService.brands$.subscribe((brands) => (this.brands = brands));
    this.brandService.getAll().subscribe();

    this.modelService.models$.subscribe((models) => (this.models = models));
    this.modelService.getAll().subscribe();
  }

  openBrandForm(brand?: Brand) {
    this.brandFormService.open(brand);
  }

  openModelForm(model?: Model) {
    this.modelFormService.open(model);
  }

  confirmDeleteBrand(id: number) {
    this.modalService
      .show({
        title: 'Confirmação de remoção',
        message: 'Deseja deletar esta marca?',
        confirmText: 'Deletar',
        cancelText: 'Cancelar',
      })
      .subscribe((confirmed) => {
        if (confirmed) this.deleteBrand(id);
        else this.modalService.close();
      });
  }

  deleteBrand(id: number) {
    this.brandService.delete(id).subscribe({
      next: () => {
        this.toastr.success('Marca deletada com sucesso', 'Sucesso');
      },
      error: (err) => {
        if (err.status === 409 && err.error.code === 'BRAND_HAS_MODELS') {
          this.toastr.error('Marca possui modelos associados.', 'Erro');
        } else {
          this.toastr.error('Erro ao deletar marca', 'Erro');
          console.error(err);
        }
      },
    });
  }

  confirmDeleteModel(id: number) {
    this.modalService
      .show({
        title: 'Confirmação de remoção',
        message: 'Deseja deletar este modelo?',
        confirmText: 'Deletar',
        cancelText: 'Cancelar',
      })
      .subscribe((confirmado) => {
        if (confirmado) this.deleteModel(id);
        else this.modalService.close();
      });
  }

  deleteModel(id: number) {
    this.modelService.delete(id).subscribe({
      next: () => {
        this.toastr.success('Modelo deletado com sucesso', 'Sucesso');
      },
      error: (err) => {
        if (err.status === 409 && err.error.code === 'MODEL_HAS_VEHICLES') {
          this.toastr.error('Modelo possui veículos associados.', 'Erro');
        } else {
          this.toastr.error('Erro ao deletar modelo', 'Erro');
          console.error(err);
        }
      },
    });
  }
}
