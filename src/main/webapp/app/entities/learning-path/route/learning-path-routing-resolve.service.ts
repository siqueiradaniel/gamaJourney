import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILearningPath } from '../learning-path.model';
import { LearningPathService } from '../service/learning-path.service';

const learningPathResolve = (route: ActivatedRouteSnapshot): Observable<null | ILearningPath> => {
  const id = route.params.id;
  if (id) {
    return inject(LearningPathService)
      .find(id)
      .pipe(
        mergeMap((learningPath: HttpResponse<ILearningPath>) => {
          if (learningPath.body) {
            return of(learningPath.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default learningPathResolve;
