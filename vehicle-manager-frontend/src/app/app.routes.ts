import { Routes } from '@angular/router';
import { Login } from './components/pages/login/login';
import { Cadastro } from './components/pages/cadastro/cadastro';
import { Layout } from './components/structure/layout/layout';
import { Dashboard } from './components/pages/dashboard/dashboard';
import { VehiclePage } from './components/pages/vehicle-page/vehicle-page';
import { UserProfile } from './components/pages/user-profile/user-profile';
import { BrandModelPage } from './components/pages/brand-model-page/brand-model-page';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Cadastro },
  {
    path: '',
    component: Layout,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard },
      { path: 'vehicle-page', component: VehiclePage },
      { path: 'profile', component: UserProfile },
      { path: 'brand-model-page', component: BrandModelPage },
    ],
  },
];
