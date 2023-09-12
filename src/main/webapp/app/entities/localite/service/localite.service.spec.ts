import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILocalite } from '../localite.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../localite.test-samples';

import { LocaliteService } from './localite.service';

const requireRestSample: ILocalite = {
  ...sampleWithRequiredData,
};

describe('Localite Service', () => {
  let service: LocaliteService;
  let httpMock: HttpTestingController;
  let expectedResult: ILocalite | ILocalite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LocaliteService);
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

    it('should create a Localite', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const localite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(localite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Localite', () => {
      const localite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(localite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Localite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Localite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Localite', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addLocaliteToCollectionIfMissing', () => {
      it('should add a Localite to an empty array', () => {
        const localite: ILocalite = sampleWithRequiredData;
        expectedResult = service.addLocaliteToCollectionIfMissing([], localite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localite);
      });

      it('should not add a Localite to an array that contains it', () => {
        const localite: ILocalite = sampleWithRequiredData;
        const localiteCollection: ILocalite[] = [
          {
            ...localite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLocaliteToCollectionIfMissing(localiteCollection, localite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Localite to an array that doesn't contain it", () => {
        const localite: ILocalite = sampleWithRequiredData;
        const localiteCollection: ILocalite[] = [sampleWithPartialData];
        expectedResult = service.addLocaliteToCollectionIfMissing(localiteCollection, localite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localite);
      });

      it('should add only unique Localite to an array', () => {
        const localiteArray: ILocalite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const localiteCollection: ILocalite[] = [sampleWithRequiredData];
        expectedResult = service.addLocaliteToCollectionIfMissing(localiteCollection, ...localiteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const localite: ILocalite = sampleWithRequiredData;
        const localite2: ILocalite = sampleWithPartialData;
        expectedResult = service.addLocaliteToCollectionIfMissing([], localite, localite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(localite);
        expect(expectedResult).toContain(localite2);
      });

      it('should accept null and undefined values', () => {
        const localite: ILocalite = sampleWithRequiredData;
        expectedResult = service.addLocaliteToCollectionIfMissing([], null, localite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(localite);
      });

      it('should return initial array if no Localite is added', () => {
        const localiteCollection: ILocalite[] = [sampleWithRequiredData];
        expectedResult = service.addLocaliteToCollectionIfMissing(localiteCollection, undefined, null);
        expect(expectedResult).toEqual(localiteCollection);
      });
    });

    describe('compareLocalite', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLocalite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLocalite(entity1, entity2);
        const compareResult2 = service.compareLocalite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLocalite(entity1, entity2);
        const compareResult2 = service.compareLocalite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLocalite(entity1, entity2);
        const compareResult2 = service.compareLocalite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
