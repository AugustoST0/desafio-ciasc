import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ActivityMonitorService } from './services/activity-monitor-service';
import { ModalComponent } from './components/modal-component/modal-component';
import { VehicleForm } from './components/forms/vehicle-form/vehicle-form';
import { BrandForm } from './components/forms/brand-form/brand-form';
import { ModelForm } from './components/forms/model-form/model-form';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ModalComponent, VehicleForm, BrandForm, ModelForm],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected readonly title = signal('vehicle-manager-frontend');

  constructor(private activityMonitorService: ActivityMonitorService) {}

  ngOnInit() {
    const token = localStorage.getItem('accessToken');
    if (token) {
      this.activityMonitorService.initMonitoring();
    }
  }
}
