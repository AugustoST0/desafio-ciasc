import { Component, OnInit } from '@angular/core';
import { BrandService } from '../../../services/api/brand-service';
import { ModelService } from '../../../services/api/model-service';
import { VehicleService } from '../../../services/api/vehicle-service';
import { Brand } from '../../../interfaces/Brand';
import { Vehicle } from '../../../interfaces/Vehicle';
import { Model } from '../../../interfaces/Model';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard implements OnInit {
  vehicles: Vehicle[] = [];
  brands: Brand[] = [];
  models: Model[] = [];

  constructor(
    private vehicleService: VehicleService,
    private brandService: BrandService,
    private modelService: ModelService
  ) {}

  ngOnInit() {
    this.vehicleService.vehicles$.subscribe((vehicles) => (this.vehicles = vehicles));
    this.vehicleService.getAll().subscribe();

    this.brandService.brands$.subscribe((brands) => (this.brands = brands));
    this.brandService.getAll().subscribe();

    this.modelService.models$.subscribe((models) => (this.models = models));
    this.modelService.getAll().subscribe();
  }
}
