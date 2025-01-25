import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISubcontent } from '../subcontent.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../subcontent.test-samples';

import { SubcontentService } from './subcontent.service';

const requireRestSample: ISubcontent = {
  ...sampleWithRequiredData,
};

describe('Subcontent Service', () => {
  let service: SubcontentService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubcontent | ISubcontent[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SubcontentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Subcontent', () => {
      const subcontent = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(subcontent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Subcontent', () => {
      const subcontent = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(subcontent).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Subcontent', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Subcontent', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Subcontent', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSubcontentToCollectionIfMissing', () => {
      it('should add a Subcontent to an empty array', () => {
        const subcontent: ISubcontent = sampleWithRequiredData;
        expectedResult = service.addSubcontentToCollectionIfMissing([], subcontent);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subcontent);
      });

      it('should not add a Subcontent to an array that contains it', () => {
        const subcontent: ISubcontent = sampleWithRequiredData;
        const subcontentCollection: ISubcontent[] = [
          {
            ...subcontent,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubcontentToCollectionIfMissing(subcontentCollection, subcontent);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Subcontent to an array that doesn't contain it", () => {
        const subcontent: ISubcontent = sampleWithRequiredData;
        const subcontentCollection: ISubcontent[] = [sampleWithPartialData];
        expectedResult = service.addSubcontentToCollectionIfMissing(subcontentCollection, subcontent);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subcontent);
      });

      it('should add only unique Subcontent to an array', () => {
        const subcontentArray: ISubcontent[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const subcontentCollection: ISubcontent[] = [sampleWithRequiredData];
        expectedResult = service.addSubcontentToCollectionIfMissing(subcontentCollection, ...subcontentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const subcontent: ISubcontent = sampleWithRequiredData;
        const subcontent2: ISubcontent = sampleWithPartialData;
        expectedResult = service.addSubcontentToCollectionIfMissing([], subcontent, subcontent2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subcontent);
        expect(expectedResult).toContain(subcontent2);
      });

      it('should accept null and undefined values', () => {
        const subcontent: ISubcontent = sampleWithRequiredData;
        expectedResult = service.addSubcontentToCollectionIfMissing([], null, subcontent, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subcontent);
      });

      it('should return initial array if no Subcontent is added', () => {
        const subcontentCollection: ISubcontent[] = [sampleWithRequiredData];
        expectedResult = service.addSubcontentToCollectionIfMissing(subcontentCollection, undefined, null);
        expect(expectedResult).toEqual(subcontentCollection);
      });
    });

    describe('compareSubcontent', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubcontent(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
        const entity2 = null;

        const compareResult1 = service.compareSubcontent(entity1, entity2);
        const compareResult2 = service.compareSubcontent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
        const entity2 = { id: 'ccf35440-d8be-425d-b236-5a95d83f4e8c' };

        const compareResult1 = service.compareSubcontent(entity1, entity2);
        const compareResult2 = service.compareSubcontent(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
        const entity2 = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };

        const compareResult1 = service.compareSubcontent(entity1, entity2);
        const compareResult2 = service.compareSubcontent(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
