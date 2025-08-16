import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Vehicle } from '../interfaces/Vehicle';

@Injectable({
  providedIn: 'root'
})
export class VehicleFormService {
  private isVisibleSubject = new BehaviorSubject<boolean>(false);
  private vehicleDataSubject = new BehaviorSubject<Vehicle | null>(null);

  isVisible$ = this.isVisibleSubject.asObservable();
  vehicleData$ = this.vehicleDataSubject.asObservable();

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
}
