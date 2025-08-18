import { Component, OnInit, OnDestroy } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { VehicleFormService } from '../../../services/form/vehicle-form-service';
import { VehicleService } from '../../../services/api/vehicle-service';
import { BrandService } from '../../../services/api/brand-service';
import { ModelService } from '../../../services/api/model-service';
import { Brand } from '../../../interfaces/Brand';
import { Model } from '../../../interfaces/Model';
import { Vehicle } from '../../../interfaces/Vehicle';

@Component({
  selector: 'app-vehicle-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vehicle-form.html',
  styleUrls: ['./vehicle-form.css'],
})
export class VehicleForm implements OnInit, OnDestroy {
  vehicleForm!: FormGroup;
  isVisible = false;
  isEditing = false;
  private subs: Subscription[] = [];

  brands: Brand[] = [];
  models: Model[] = [];
  filteredModels: Model[] = [];

  constructor(
    private fb: FormBuilder,
    private vehicleFormService: VehicleFormService,
    private vehicleService: VehicleService,
    private toastr: ToastrService,
    private brandService: BrandService,
    private modelService: ModelService
  ) {}

  ngOnInit() {
    this.vehicleForm = this.fb.group({
      id: [null],
      brand: ['', Validators.required],
      model: ['', Validators.required],
      year: ['', [Validators.required, Validators.min(1886)]],
      plate: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Z]{3}[0-9][A-Z][0-9]{2}$/),
        ],
      ],
      vehicleType: ['', Validators.required],
    });

    // Observables de visibilidade e dados do formulário
    this.subs.push(
      this.vehicleFormService.isVisible$.subscribe((v) => (this.isVisible = v)),
      this.vehicleFormService.vehicleData$.subscribe((vehicle) => {
        if (vehicle) {
          this.vehicleForm.patchValue({
            id: vehicle.id,
            brand: vehicle.model.brand.id,
            model: vehicle.model.id,
            year: vehicle.year,
            plate: vehicle.plate,
            vehicleType: vehicle.vehicleType,
          });
          this.isEditing = true;
          this.updateFilteredModels(vehicle.model.brand.id);
        } else {
          this.vehicleForm.reset();
          this.isEditing = false;
          this.updateFilteredModels();
        }
      })
    );

    // Inscrições para listas de marcas e modelos
    this.subs.push(
      this.brandService.brands$.subscribe((brands) => {
        this.brands = brands;
      }),
      this.modelService.models$.subscribe((models) => {
        this.models = models;
        this.updateFilteredModels();
      })
    );

    // Carregamento inicial
    this.brandService.getAll().subscribe();
    this.modelService.getAll().subscribe();

    // Lógica de dependência entre marca e modelo
    this.subs.push(
      // usuário muda marca, modelos são limitados
      this.vehicleForm.get('brand')!.valueChanges.subscribe((brandId) => {
        this.updateFilteredModels(brandId);

        const selectedModelId = this.vehicleForm.get('model')!.value;
        if (
          selectedModelId &&
          !this.filteredModels.some((m) => m.id === selectedModelId)
        ) {
          this.vehicleForm.get('model')!.setValue('');
        }
      }),
      // usuário muda modelo, marca é preenchida automaticamente
      this.vehicleForm.get('model')!.valueChanges.subscribe((modelId) => {
        const model = this.models.find((m) => m.id === +modelId);
        if (model) {
          this.vehicleForm.get('brand')!.setValue(model.brand.id, {
            emitEvent: false,
          });
          this.updateFilteredModels(model.brand.id);
        }
      })
    );
  }

  private updateFilteredModels(brandId?: number) {
    if (brandId) {
      this.filteredModels = this.models.filter((m) => m.brand.id === +brandId);
    } else {
      this.filteredModels = this.models.slice();
    }
  }

  onSubmit() {
    const payload: Vehicle = {
      model: this.models.find((m) => m.id === +this.vehicleForm.value.model)!,
      year: this.vehicleForm.value.year,
      plate: this.vehicleForm.value.plate,
      vehicleType: this.vehicleForm.value.vehicleType,
    };

    if (this.isEditing && this.vehicleForm.value.id) {
      if (this.vehicleForm.pristine) {
        this.toastr.info(
          'Você deve fazer alterações',
          'Nenhuma mudança detectada'
        );
        return;
      }
      this.updateVehicle(payload);
    } else {
      this.insertVehicle(payload);
    }
  }

  updateVehicle(payload: Vehicle) {
    this.vehicleService.update(this.vehicleForm.value.id, payload).subscribe({
      next: () => {
        this.toastr.success('Veículo atualizado com sucesso', 'Sucesso');
        this.close();
      },
      error: (err) => {
        this.toastr.error('Erro ao atualizar veículo', 'Erro');
        console.error(err);
      },
    });
  }

  insertVehicle(payload: Vehicle) {
    this.vehicleService.insert(payload).subscribe({
      next: () => {
        this.toastr.success('Veículo adicionado com sucesso', 'Sucesso');
        this.close();
      },
      error: (err) => {
        this.toastr.error('Erro ao adicionar veículo', 'Erro');
        console.error(err);
      },
    });
  }

  close() {
    this.vehicleFormService.close();
  }

  ngOnDestroy() {
    this.subs.forEach((s) => s.unsubscribe());
  }
}
