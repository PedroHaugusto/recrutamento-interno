import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'login',
    title: 'Entrar · Recrutamento Interno',
    loadComponent: () =>
      import('./auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'registro',
    title: 'Criar conta · Recrutamento Interno',
    loadComponent: () =>
      import('./auth/register/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: '',
    loadComponent: () =>
      import('./core/layouts/layout.component').then((m) => m.LayoutComponent),
    canActivate: [authGuard],
    children: [
      {
        path: 'vagas',
        title: 'Vagas · Recrutamento Interno',
        loadComponent: () =>
          import('./vagas/vagas-list/vagas-list.component').then((m) => m.VagasListComponent)
      },
      {
        path: 'candidaturas/minhas',
        title: 'Minhas Candidaturas · Recrutamento Interno',
        loadComponent: () =>
          import('./candidaturas/minhas-candidaturas/minhas-candidaturas.component').then(
            (m) => m.MinhasCandidaturasComponent
          )
      }
    ]
  }
];
