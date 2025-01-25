import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import SubcontentResolve from './route/subcontent-routing-resolve.service';

const subcontentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/subcontent.component').then(m => m.SubcontentComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/subcontent-detail.component').then(m => m.SubcontentDetailComponent),
    resolve: {
      subcontent: SubcontentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/subcontent-update.component').then(m => m.SubcontentUpdateComponent),
    resolve: {
      subcontent: SubcontentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/subcontent-update.component').then(m => m.SubcontentUpdateComponent),
    resolve: {
      subcontent: SubcontentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default subcontentRoute;
