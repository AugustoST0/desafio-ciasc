import { Routes } from '@angular/router';
import { Login } from './components/pages/login/login';
import { Cadastro } from './components/pages/cadastro/cadastro';
import { Layout } from './components/structure/layout/layout';
import { Dashboard } from './components/pages/dashboard/dashboard';
import { VehiclePage } from './components/pages/vehicle-page/vehicle-page';
import { UserProfile } from './components/pages/user-profile/user-profile';
import { BrandModelPage } from './components/pages/brand-model-page/brand-model-page';
import { AuthGuard } from './services/guards/auth-guard';
import { AdminGuard } from './services/guards/admin-guard';

export const routes: Routes = [
  { path: 'login', component: Login },
  {
    path: '',
    component: Layout,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: Dashboard },
      { path: 'vehicle-page', component: VehiclePage },
      { path: 'profile', component: UserProfile },
      {
        path: 'admin',
        canActivate: [AdminGuard],
        children: [
          { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
          { path: 'brand-model-page', component: BrandModelPage },
          { path: 'register', component: Cadastro },
        ],
      },
    ],
  },
];
