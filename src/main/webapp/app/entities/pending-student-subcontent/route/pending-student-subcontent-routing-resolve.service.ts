import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPendingStudentSubcontent } from '../pending-student-subcontent.model';
import { PendingStudentSubcontentService } from '../service/pending-student-subcontent.service';

const pendingStudentSubcontentResolve = (route: ActivatedRouteSnapshot): Observable<null | IPendingStudentSubcontent> => {
  const id = route.params.id;
  if (id) {
    return inject(PendingStudentSubcontentService)
      .find(id)
      .pipe(
        mergeMap((pendingStudentSubcontent: HttpResponse<IPendingStudentSubcontent>) => {
          if (pendingStudentSubcontent.body) {
            return of(pendingStudentSubcontent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default pendingStudentSubcontentResolve;
