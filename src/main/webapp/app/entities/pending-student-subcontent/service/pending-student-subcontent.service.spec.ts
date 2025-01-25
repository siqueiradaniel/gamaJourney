import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPendingStudentSubcontent } from '../pending-student-subcontent.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../pending-student-subcontent.test-samples';

import { PendingStudentSubcontentService } from './pending-student-subcontent.service';

const requireRestSample: IPendingStudentSubcontent = {
  ...sampleWithRequiredData,
};

describe('PendingStudentSubcontent Service', () => {
  let service: PendingStudentSubcontentService;
  let httpMock: HttpTestingController;
  let expectedResult: IPendingStudentSubcontent | IPendingStudentSubcontent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PendingStudentSubcontentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PendingStudentSubcontent', () => {
      const pendingStudentSubcontent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pendingStudentSubcontent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PendingStudentSubcontent', () => {
      const pendingStudentSubcontent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pendingStudentSubcontent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PendingStudentSubcontent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PendingStudentSubcontent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PendingStudentSubcontent', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPendingStudentSubcontentToCollectionIfMissing', () => {
      it('should add a PendingStudentSubcontent to an empty array', () => {
        const pendingStudentSubcontent: IPendingStudentSubcontent = sampleWithRequiredData;
        expectedResult = service.addPendingStudentSubcontentToCollectionIfMissing([], pendingStudentSubcontent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pendingStudentSubcontent);
      });

      it('should not add a PendingStudentSubcontent to an array that contains it', () => {
        const pendingStudentSubcontent: IPendingStudentSubcontent = sampleWithRequiredData;
        const pendingStudentSubcontentCollection: IPendingStudentSubcontent[] = [
          {
            ...pendingStudentSubcontent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPendingStudentSubcontentToCollectionIfMissing(
          pendingStudentSubcontentCollection,
          pendingStudentSubcontent,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PendingStudentSubcontent to an array that doesn't contain it", () => {
        const pendingStudentSubcontent: IPendingStudentSubcontent = sampleWithRequiredData;
        const pendingStudentSubcontentCollection: IPendingStudentSubcontent[] = [sampleWithPartialData];
        expectedResult = service.addPendingStudentSubcontentToCollectionIfMissing(
          pendingStudentSubcontentCollection,
          pendingStudentSubcontent,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pendingStudentSubcontent);
      });

      it('should add only unique PendingStudentSubcontent to an array', () => {
        const pendingStudentSubcontentArray: IPendingStudentSubcontent[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const pendingStudentSubcontentCollection: IPendingStudentSubcontent[] = [sampleWithRequiredData];
        expectedResult = service.addPendingStudentSubcontentToCollectionIfMissing(
          pendingStudentSubcontentCollection,
          ...pendingStudentSubcontentArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pendingStudentSubcontent: IPendingStudentSubcontent = sampleWithRequiredData;
        const pendingStudentSubcontent2: IPendingStudentSubcontent = sampleWithPartialData;
        expectedResult = service.addPendingStudentSubcontentToCollectionIfMissing([], pendingStudentSubcontent, pendingStudentSubcontent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pendingStudentSubcontent);
        expect(expectedResult).toContain(pendingStudentSubcontent2);
      });

      it('should accept null and undefined values', () => {
        const pendingStudentSubcontent: IPendingStudentSubcontent = sampleWithRequiredData;
        expectedResult = service.addPendingStudentSubcontentToCollectionIfMissing([], null, pendingStudentSubcontent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pendingStudentSubcontent);
      });

      it('should return initial array if no PendingStudentSubcontent is added', () => {
        const pendingStudentSubcontentCollection: IPendingStudentSubcontent[] = [sampleWithRequiredData];
        expectedResult = service.addPendingStudentSubcontentToCollectionIfMissing(pendingStudentSubcontentCollection, undefined, null);
        expect(expectedResult).toEqual(pendingStudentSubcontentCollection);
      });
    });

    describe('comparePendingStudentSubcontent', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePendingStudentSubcontent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 29412 };
        const entity2 = null;

        const compareResult1 = service.comparePendingStudentSubcontent(entity1, entity2);
        const compareResult2 = service.comparePendingStudentSubcontent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 29412 };
        const entity2 = { id: 1204 };

        const compareResult1 = service.comparePendingStudentSubcontent(entity1, entity2);
        const compareResult2 = service.comparePendingStudentSubcontent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 29412 };
        const entity2 = { id: 29412 };

        const compareResult1 = service.comparePendingStudentSubcontent(entity1, entity2);
        const compareResult2 = service.comparePendingStudentSubcontent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
