import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDependency } from '../dependency.model';
import { DependencyService } from '../service/dependency.service';

const dependencyResolve = (route: ActivatedRouteSnapshot): Observable<null | IDependency> => {
  const id = route.params.id;
  if (id) {
    return inject(DependencyService)
      .find(id)
      .pipe(
        mergeMap((dependency: HttpResponse<IDependency>) => {
          if (dependency.body) {
            return of(dependency.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default dependencyResolve;
