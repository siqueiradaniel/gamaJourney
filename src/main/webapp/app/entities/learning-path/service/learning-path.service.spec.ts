import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ILearningPath } from '../learning-path.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../learning-path.test-samples';

import { LearningPathService } from './learning-path.service';

const requireRestSample: ILearningPath = {
  ...sampleWithRequiredData,
};

describe('LearningPath Service', () => {
  let service: LearningPathService;
  let httpMock: HttpTestingController;
  let expectedResult: ILearningPath | ILearningPath[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(LearningPathService);
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

    it('should create a LearningPath', () => {
      const learningPath = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(learningPath).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a LearningPath', () => {
      const learningPath = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(learningPath).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a LearningPath', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of LearningPath', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a LearningPath', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLearningPathToCollectionIfMissing', () => {
      it('should add a LearningPath to an empty array', () => {
        const learningPath: ILearningPath = sampleWithRequiredData;
        expectedResult = service.addLearningPathToCollectionIfMissing([], learningPath);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(learningPath);
      });

      it('should not add a LearningPath to an array that contains it', () => {
        const learningPath: ILearningPath = sampleWithRequiredData;
        const learningPathCollection: ILearningPath[] = [
          {
            ...learningPath,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLearningPathToCollectionIfMissing(learningPathCollection, learningPath);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a LearningPath to an array that doesn't contain it", () => {
        const learningPath: ILearningPath = sampleWithRequiredData;
        const learningPathCollection: ILearningPath[] = [sampleWithPartialData];
        expectedResult = service.addLearningPathToCollectionIfMissing(learningPathCollection, learningPath);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(learningPath);
      });

      it('should add only unique LearningPath to an array', () => {
        const learningPathArray: ILearningPath[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const learningPathCollection: ILearningPath[] = [sampleWithRequiredData];
        expectedResult = service.addLearningPathToCollectionIfMissing(learningPathCollection, ...learningPathArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const learningPath: ILearningPath = sampleWithRequiredData;
        const learningPath2: ILearningPath = sampleWithPartialData;
        expectedResult = service.addLearningPathToCollectionIfMissing([], learningPath, learningPath2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(learningPath);
        expect(expectedResult).toContain(learningPath2);
      });

      it('should accept null and undefined values', () => {
        const learningPath: ILearningPath = sampleWithRequiredData;
        expectedResult = service.addLearningPathToCollectionIfMissing([], null, learningPath, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(learningPath);
      });

      it('should return initial array if no LearningPath is added', () => {
        const learningPathCollection: ILearningPath[] = [sampleWithRequiredData];
        expectedResult = service.addLearningPathToCollectionIfMissing(learningPathCollection, undefined, null);
        expect(expectedResult).toEqual(learningPathCollection);
      });
    });

    describe('compareLearningPath', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLearningPath(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 6936 };
        const entity2 = null;

        const compareResult1 = service.compareLearningPath(entity1, entity2);
        const compareResult2 = service.compareLearningPath(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 6936 };
        const entity2 = { id: 25742 };

        const compareResult1 = service.compareLearningPath(entity1, entity2);
        const compareResult2 = service.compareLearningPath(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 6936 };
        const entity2 = { id: 6936 };

        const compareResult1 = service.compareLearningPath(entity1, entity2);
        const compareResult2 = service.compareLearningPath(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
