import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ExamResolve from './route/exam-routing-resolve.service';

const examRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/exam.component').then(m => m.ExamComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/exam-detail.component').then(m => m.ExamDetailComponent),
    resolve: {
      exam: ExamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/exam-update.component').then(m => m.ExamUpdateComponent),
    resolve: {
      exam: ExamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/exam-update.component').then(m => m.ExamUpdateComponent),
    resolve: {
      exam: ExamResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default examRoute;
