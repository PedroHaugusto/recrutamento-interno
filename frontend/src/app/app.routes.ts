import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () =>
      import('./auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'registro',
    loadComponent: () =>
      import('./auth/register/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: '',
    loadComponent: () =>
      import('./core/layout/layout.component').then((m) => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: 'vagas',
        loadComponent: () =>
          import('./vagas/vagas-list/vagas-list.component').then((m) => m.VagasListComponent)
      },
      {
        path: 'candidaturas/minhas',
        loadComponent: () =>
          import('./candidaturas/minhas-candidaturas/minhas-candidaturas.component').then(
            (m) => m.MinhasCandidaturasComponent
          )
      }
    ]
  }
];
