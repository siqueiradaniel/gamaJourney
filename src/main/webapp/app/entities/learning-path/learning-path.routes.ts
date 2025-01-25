import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LearningPathResolve from './route/learning-path-routing-resolve.service';

const learningPathRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/learning-path.component').then(m => m.LearningPathComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/learning-path-detail.component').then(m => m.LearningPathDetailComponent),
    resolve: {
      learningPath: LearningPathResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/learning-path-update.component').then(m => m.LearningPathUpdateComponent),
    resolve: {
      learningPath: LearningPathResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/learning-path-update.component').then(m => m.LearningPathUpdateComponent),
    resolve: {
      learningPath: LearningPathResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default learningPathRoute;
