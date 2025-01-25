import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubcontent } from '../subcontent.model';
import { SubcontentService } from '../service/subcontent.service';

const subcontentResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubcontent> => {
  const id = route.params.id;
  if (id) {
    return inject(SubcontentService)
      .find(id)
      .pipe(
        mergeMap((subcontent: HttpResponse<ISubcontent>) => {
          if (subcontent.body) {
            return of(subcontent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default subcontentResolve;
