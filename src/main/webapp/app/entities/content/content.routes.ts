import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ContentResolve from './route/content-routing-resolve.service';

const contentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/content.component').then(m => m.ContentComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/content-detail.component').then(m => m.ContentDetailComponent),
    resolve: {
      content: ContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/content-update.component').then(m => m.ContentUpdateComponent),
    resolve: {
      content: ContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/content-update.component').then(m => m.ContentUpdateComponent),
    resolve: {
      content: ContentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contentRoute;
