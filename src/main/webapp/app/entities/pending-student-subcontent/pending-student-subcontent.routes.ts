import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PendingStudentSubcontentResolve from './route/pending-student-subcontent-routing-resolve.service';

const pendingStudentSubcontentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pending-student-subcontent.component').then(m => m.PendingStudentSubcontentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () =>
      import('./detail/pending-student-subcontent-detail.component').then(m => m.PendingStudentSubcontentDetailComponent),
    resolve: {
      pendingStudentSubcontent: PendingStudentSubcontentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./update/pending-student-subcontent-update.component').then(m => m.PendingStudentSubcontentUpdateComponent),
    resolve: {
      pendingStudentSubcontent: PendingStudentSubcontentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./update/pending-student-subcontent-update.component').then(m => m.PendingStudentSubcontentUpdateComponent),
    resolve: {
      pendingStudentSubcontent: PendingStudentSubcontentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default pendingStudentSubcontentRoute;
