import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IPendingStudentSubcontent } from '../pending-student-subcontent.model';
import { PendingStudentSubcontentService } from '../service/pending-student-subcontent.service';

import pendingStudentSubcontentResolve from './pending-student-subcontent-routing-resolve.service';

describe('PendingStudentSubcontent routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PendingStudentSubcontentService;
  let resultPendingStudentSubcontent: IPendingStudentSubcontent | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(PendingStudentSubcontentService);
    resultPendingStudentSubcontent = undefined;
  });

  describe('resolve', () => {
    it('should return IPendingStudentSubcontent returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        pendingStudentSubcontentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPendingStudentSubcontent = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultPendingStudentSubcontent).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        pendingStudentSubcontentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPendingStudentSubcontent = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toHaveBeenCalled();
      expect(resultPendingStudentSubcontent).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPendingStudentSubcontent>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        pendingStudentSubcontentResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPendingStudentSubcontent = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultPendingStudentSubcontent).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
