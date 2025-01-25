import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DependencyResolve from './route/dependency-routing-resolve.service';

const dependencyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/dependency.component').then(m => m.DependencyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/dependency-detail.component').then(m => m.DependencyDetailComponent),
    resolve: {
      dependency: DependencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/dependency-update.component').then(m => m.DependencyUpdateComponent),
    resolve: {
      dependency: DependencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/dependency-update.component').then(m => m.DependencyUpdateComponent),
    resolve: {
      dependency: DependencyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default dependencyRoute;
