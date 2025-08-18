import { Component, OnInit } from '@angular/core';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';
import { RouterModule } from '@angular/router';
import { VehicleForm } from '../../forms/vehicle-form/vehicle-form';
import { BrandForm } from '../../forms/brand-form/brand-form';
import { ModelForm } from '../../forms/model-form/model-form';

@Component({
  selector: 'app-layout',
  imports: [Header, Footer, RouterModule, VehicleForm, BrandForm, ModelForm],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout {
  
}
