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
  private currentId: number | null = null;
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
            brand: vehicle.brand.name,
            model: vehicle.model.name,
            year: vehicle.year,
            plate: vehicle.plate,
            vehicleType: vehicle.vehicleType,
          });
          this.isEditing = true;
          this.currentId = vehicle.id!;
          this.updateFilteredModels(vehicle.brand.name);
        } else {
          this.vehicleForm.reset();
          this.isEditing = false;
          this.currentId = null;
          this.filteredModels = this.models.slice();
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
        this.filteredModels = models.slice();
      })
    );

    // Carregamento inicial
    this.brandService
      .getAll()
      .subscribe({ error: (err) => console.error(err) });
    this.modelService
      .getAll()
      .subscribe({ error: (err) => console.error(err) });

    // Lógica de dependência entre marca e modelo
    this.subs.push(
      // usuário muda marca, modelos são limitados
      this.vehicleForm.get('brand')!.valueChanges.subscribe((brandName) => {
        this.updateFilteredModels(brandName);

        // Se o modelo selecionado não pertence à marca, limpa o campo modelo
        const selectedModelName = this.vehicleForm.get('model')!.value;
        if (
          selectedModelName &&
          !this.filteredModels.some((m) => m.name === selectedModelName)
        ) {
          this.vehicleForm.get('model')!.setValue('');
        }
      }),
      // usuário muda modelo, marca é preenchida automaticamente
      this.vehicleForm.get('model')!.valueChanges.subscribe((modelName) => {
        const model = this.models.find((m) => m.name === modelName);
        if (model) {
          this.vehicleForm.get('brand')!.setValue(model.brand.name, {
            emitEvent: false, // evita loop infinito
          });
          this.updateFilteredModels(model.brand.name);
        }
      })
    );
  }

  private updateFilteredModels(brandName: string) {
    if (!brandName) {
      this.filteredModels = this.models.slice();
    } else {
      this.filteredModels = this.models.filter(
        (m) => m.brand.name === brandName
      );
    }
  }

  onSubmit() {
    if (this.isEditing && this.currentId) {
      if (this.vehicleForm.pristine) {
        this.toastr.info(
          'Você deve fazer alterações',
          'Nenhuma mudança detectada'
        );
        return;
      }
      this.updateVehicle();
    } else {
      this.insertVehicle();
    }
  }

  updateVehicle() {
    const payload: Vehicle = {
      brand: this.brands.find((b) => b.name === this.vehicleForm.value.brand)!,
      model: this.models.find((m) => m.name === this.vehicleForm.value.model)!,
      year: this.vehicleForm.value.year,
      plate: this.vehicleForm.value.plate,
      vehicleType: this.vehicleForm.value.vehicleType,
    };

    this.vehicleService
      .update(this.vehicleForm.value.id, payload)
      .subscribe({
        next: (updatedVehicle) => {
          this.toastr.success('Veículo atualizado com sucesso', 'Sucesso');
          this.vehicleFormService.notifyVehicleSaved(updatedVehicle, true);
          this.close();
        },
        error: (err) => {
          this.toastr.error('Erro ao atualizar veículo', 'Erro');
          console.error(err);
        },
      });
  }

  insertVehicle() {
    const payload: Vehicle = {
      brand: this.brands.find((b) => b.name === this.vehicleForm.value.brand)!,
      model: this.models.find((m) => m.name === this.vehicleForm.value.model)!,
      year: this.vehicleForm.value.year,
      plate: this.vehicleForm.value.plate,
      vehicleType: this.vehicleForm.value.vehicleType,
    };

    this.vehicleService.insert(payload).subscribe({
      next: (newVehicle) => {
        this.toastr.success('Veículo adicionado com sucesso', 'Sucesso');
        this.vehicleFormService.notifyVehicleSaved(newVehicle, false);
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
