import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'gamaJourneyApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'student',
    data: { pageTitle: 'gamaJourneyApp.student.home.title' },
    loadChildren: () => import('./student/student.routes'),
  },
  {
    path: 'exam',
    data: { pageTitle: 'gamaJourneyApp.exam.home.title' },
    loadChildren: () => import('./exam/exam.routes'),
  },
  {
    path: 'content',
    data: { pageTitle: 'gamaJourneyApp.content.home.title' },
    loadChildren: () => import('./content/content.routes'),
  },
  {
    path: 'subcontent',
    data: { pageTitle: 'gamaJourneyApp.subcontent.home.title' },
    loadChildren: () => import('./subcontent/subcontent.routes'),
  },
  {
    path: 'learning-path',
    data: { pageTitle: 'gamaJourneyApp.learningPath.home.title' },
    loadChildren: () => import('./learning-path/learning-path.routes'),
  },
  {
    path: 'dependency',
    data: { pageTitle: 'gamaJourneyApp.dependency.home.title' },
    loadChildren: () => import('./dependency/dependency.routes'),
  },
  {
    path: 'pending-student-subcontent',
    data: { pageTitle: 'gamaJourneyApp.pendingStudentSubcontent.home.title' },
    loadChildren: () => import('./pending-student-subcontent/pending-student-subcontent.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
