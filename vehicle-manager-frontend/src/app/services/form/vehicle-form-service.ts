import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';
import { Vehicle } from '../../interfaces/Vehicle';

@Injectable({
  providedIn: 'root',
})
export class VehicleFormService {
  private isVisibleSubject = new BehaviorSubject<boolean>(false);
  private vehicleDataSubject = new BehaviorSubject<Vehicle | null>(null);
  private vehicleInsertedSubject = new Subject<Vehicle>();
  private vehicleUpdatedSubject = new Subject<Vehicle>();

  isVisible$ = this.isVisibleSubject.asObservable();
  vehicleData$ = this.vehicleDataSubject.asObservable();
  vehicleInserted$ = this.vehicleInsertedSubject.asObservable();
  vehicleUpdated$ = this.vehicleUpdatedSubject.asObservable();

  open(vehicle?: Vehicle) {
    if (vehicle) {
      this.vehicleDataSubject.next(vehicle); // editando veículo
    } else {
      this.vehicleDataSubject.next(null); // criando veículo
    }
    this.isVisibleSubject.next(true);
  }

  close() {
    this.isVisibleSubject.next(false);
  }

  notifyVehicleSaved(vehicle: Vehicle, isEditing: boolean) {
    if (isEditing) {
      this.vehicleUpdatedSubject.next(vehicle);
    } else {
      this.vehicleInsertedSubject.next(vehicle);
    }
  }
}
