import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Vehicle } from '../../../interfaces/Vehicle';
import { VehicleService } from '../../../services/api/vehicle-service';
import { VehicleFormService } from '../../../services/form/vehicle-form-service';
import { ToastrService } from 'ngx-toastr';
import { ModalService } from '../../../services/modal-service';

@Component({
  selector: 'app-vehicle-page',
  imports: [CommonModule],
  templateUrl: './vehicle-page.html',
  styleUrl: './vehicle-page.css',
})
export class VehiclePage implements OnInit {
  vehicles: Vehicle[] = [];

  constructor(
    private vehicleService: VehicleService,
    private vehicleFormService: VehicleFormService,
    private toastr: ToastrService,
    private modalService: ModalService
  ) {}

  ngOnInit() {
    this.vehicleService.getAll().subscribe({
      next: (vehicles: Vehicle[]) => {
        this.vehicles = vehicles;
      },
      error: (err) => {
        this.toastr.error('Erro ao resgatar itens', 'Erro');
        console.error(err);
      },
    });

    this.vehicleFormService.vehicleInserted$.subscribe((vehicle: Vehicle) => {
      this.vehicles.push(vehicle);
    });

    this.vehicleFormService.vehicleUpdated$.subscribe((vehicle: Vehicle) => {
      const index = this.vehicles.findIndex((v) => v.id === vehicle.id);
      if (index !== -1) {
        this.vehicles[index] = vehicle;
      }
    });
  }

  openFormToCreate() {
    this.vehicleFormService.open();
  }

  openFormToEdit(vehicle: Vehicle) {
    this.vehicleFormService.open(vehicle);
  }

  confirmDeleteVehicle(id: number) {
    this.modalService
      .show({
        title: 'Confirmação de remoção',
        message: 'Esta ação é irreversível. Deseja deletar o veículo?',
        confirmText: 'Deletar',
        cancelText: 'Cancelar',
      })
      .subscribe((confirmado) => {
        if (confirmado) {
          this.deleteVehicle(id);
        } else {
          this.modalService.close();
        }
      });
  }

  deleteVehicle(id: number) {
    this.vehicleService.delete(id).subscribe({
      next: () => {
        this.vehicles = this.vehicles.filter((v) => v.id !== id);
        this.toastr.success('O veículo foi deletado com sucesso', 'Sucesso');
      },
      error: (err) => {
        this.toastr.error('Erro ao deletar veículo', 'Erro');
        console.error(err);
      },
    });
  }
}
