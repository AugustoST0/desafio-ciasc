import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { VehicleFormService } from '../../services/vehicle-form-service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { CommonModule } from '@angular/common';
import { VehicleService } from '../../services/vehicle-service';

@Component({
  selector: 'app-vehicle-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vehicle-form.html',
  styleUrls: ['./vehicle-form.css'],
})
export class VehicleForm implements OnInit {
  form!: FormGroup;
  isVisible = false;
  private subs: Subscription[] = [];

  constructor(
    private fb: FormBuilder,
    private vehicleFormService: VehicleFormService,
    private vehicleService: VehicleService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      brand: ['', Validators.required],
      model: ['', Validators.required],
      year: ['', [Validators.required, Validators.min(1886)]],
      plate: ['', Validators.required],
      vehicleType: ['', Validators.required],
    });

    this.subs.push(
      this.vehicleFormService.isVisible$.subscribe((v) => (this.isVisible = v)),
      this.vehicleFormService.vehicleData$.subscribe((vehicle) => {
        if (vehicle) {
          this.form.patchValue(vehicle);
        } else {
          this.form.reset();
        }
      })
    );
  }

  onSubmit() {
    if (this.form.invalid) {
      this.toastr.error('Preencha todos os campos corretamente', 'Erro');
      return;
    }

    this.vehicleService.insert(this.form.value).subscribe({
      next: () => {
        this.toastr.info('Veículo adicionado com sucesso', 'Sucesso');
      },
      error: (err) => {
        this.toastr.error('Erro ao adicionar item', 'Erro');
        console.error(err);
      }
    })
    this.vehicleFormService.close();
  }

  close() {
    this.vehicleFormService.close();
  }

  ngOnDestroy() {
    this.subs.forEach((s) => s.unsubscribe());
  }
}
