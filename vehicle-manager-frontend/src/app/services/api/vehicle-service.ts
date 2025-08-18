import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Vehicle } from '../../interfaces/Vehicle';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  baseApiUrl = environment.baseApiUrl;
  apiUrl = `${this.baseApiUrl}/vehicles`;

  private vehiclesSubject = new BehaviorSubject<Vehicle[]>([]);
  vehicles$ = this.vehiclesSubject.asObservable();

  constructor(private http: HttpClient) {}

  getAll(): Observable<Vehicle[]> {
    return this.http
      .get<Vehicle[]>(this.apiUrl)
      .pipe(tap((vehicles) => this.vehiclesSubject.next(vehicles)));
  }

  getById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
  }

  insert(vehicle: Vehicle): Observable<Vehicle> {
    return this.http.post<Vehicle>(this.apiUrl, vehicle).pipe(
      tap((newVehicle) => {
        const current = this.vehiclesSubject.getValue();
        this.vehiclesSubject.next([...current, newVehicle]);
      })
    );
  }

  update(id: number, vehicle: Vehicle): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.apiUrl}/${id}`, vehicle).pipe(
      tap((updatedVehicle) => {
        const current = this.vehiclesSubject.getValue();
        const index = current.findIndex((v) => v.id === id);
        current[index] = updatedVehicle;
        this.vehiclesSubject.next([...current]);
      })
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => {
        const current = this.vehiclesSubject.getValue();
        this.vehiclesSubject.next(current.filter((v) => v.id !== id));
      })
    );
  }
}
