import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDependency } from '../dependency.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../dependency.test-samples';

import { DependencyService } from './dependency.service';

const requireRestSample: IDependency = {
  ...sampleWithRequiredData,
};

describe('Dependency Service', () => {
  let service: DependencyService;
  let httpMock: HttpTestingController;
  let expectedResult: IDependency | IDependency[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DependencyService);
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

    it('should create a Dependency', () => {
      const dependency = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dependency).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dependency', () => {
      const dependency = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dependency).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dependency', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dependency', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Dependency', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDependencyToCollectionIfMissing', () => {
      it('should add a Dependency to an empty array', () => {
        const dependency: IDependency = sampleWithRequiredData;
        expectedResult = service.addDependencyToCollectionIfMissing([], dependency);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependency);
      });

      it('should not add a Dependency to an array that contains it', () => {
        const dependency: IDependency = sampleWithRequiredData;
        const dependencyCollection: IDependency[] = [
          {
            ...dependency,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDependencyToCollectionIfMissing(dependencyCollection, dependency);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dependency to an array that doesn't contain it", () => {
        const dependency: IDependency = sampleWithRequiredData;
        const dependencyCollection: IDependency[] = [sampleWithPartialData];
        expectedResult = service.addDependencyToCollectionIfMissing(dependencyCollection, dependency);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependency);
      });

      it('should add only unique Dependency to an array', () => {
        const dependencyArray: IDependency[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dependencyCollection: IDependency[] = [sampleWithRequiredData];
        expectedResult = service.addDependencyToCollectionIfMissing(dependencyCollection, ...dependencyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dependency: IDependency = sampleWithRequiredData;
        const dependency2: IDependency = sampleWithPartialData;
        expectedResult = service.addDependencyToCollectionIfMissing([], dependency, dependency2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dependency);
        expect(expectedResult).toContain(dependency2);
      });

      it('should accept null and undefined values', () => {
        const dependency: IDependency = sampleWithRequiredData;
        expectedResult = service.addDependencyToCollectionIfMissing([], null, dependency, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dependency);
      });

      it('should return initial array if no Dependency is added', () => {
        const dependencyCollection: IDependency[] = [sampleWithRequiredData];
        expectedResult = service.addDependencyToCollectionIfMissing(dependencyCollection, undefined, null);
        expect(expectedResult).toEqual(dependencyCollection);
      });
    });

    describe('compareDependency', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDependency(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 6675 };
        const entity2 = null;

        const compareResult1 = service.compareDependency(entity1, entity2);
        const compareResult2 = service.compareDependency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 6675 };
        const entity2 = { id: 8386 };

        const compareResult1 = service.compareDependency(entity1, entity2);
        const compareResult2 = service.compareDependency(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 6675 };
        const entity2 = { id: 6675 };

        const compareResult1 = service.compareDependency(entity1, entity2);
        const compareResult2 = service.compareDependency(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
