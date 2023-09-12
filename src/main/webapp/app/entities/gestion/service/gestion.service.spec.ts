import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGestion } from '../gestion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../gestion.test-samples';

import { GestionService } from './gestion.service';

const requireRestSample: IGestion = {
  ...sampleWithRequiredData,
};

describe('Gestion Service', () => {
  let service: GestionService;
  let httpMock: HttpTestingController;
  let expectedResult: IGestion | IGestion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GestionService);
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

    it('should create a Gestion', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gestion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Gestion', () => {
      const gestion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Gestion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Gestion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Gestion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGestionToCollectionIfMissing', () => {
      it('should add a Gestion to an empty array', () => {
        const gestion: IGestion = sampleWithRequiredData;
        expectedResult = service.addGestionToCollectionIfMissing([], gestion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gestion);
      });

      it('should not add a Gestion to an array that contains it', () => {
        const gestion: IGestion = sampleWithRequiredData;
        const gestionCollection: IGestion[] = [
          {
            ...gestion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGestionToCollectionIfMissing(gestionCollection, gestion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Gestion to an array that doesn't contain it", () => {
        const gestion: IGestion = sampleWithRequiredData;
        const gestionCollection: IGestion[] = [sampleWithPartialData];
        expectedResult = service.addGestionToCollectionIfMissing(gestionCollection, gestion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gestion);
      });

      it('should add only unique Gestion to an array', () => {
        const gestionArray: IGestion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gestionCollection: IGestion[] = [sampleWithRequiredData];
        expectedResult = service.addGestionToCollectionIfMissing(gestionCollection, ...gestionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gestion: IGestion = sampleWithRequiredData;
        const gestion2: IGestion = sampleWithPartialData;
        expectedResult = service.addGestionToCollectionIfMissing([], gestion, gestion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gestion);
        expect(expectedResult).toContain(gestion2);
      });

      it('should accept null and undefined values', () => {
        const gestion: IGestion = sampleWithRequiredData;
        expectedResult = service.addGestionToCollectionIfMissing([], null, gestion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gestion);
      });

      it('should return initial array if no Gestion is added', () => {
        const gestionCollection: IGestion[] = [sampleWithRequiredData];
        expectedResult = service.addGestionToCollectionIfMissing(gestionCollection, undefined, null);
        expect(expectedResult).toEqual(gestionCollection);
      });
    });

    describe('compareGestion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGestion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGestion(entity1, entity2);
        const compareResult2 = service.compareGestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGestion(entity1, entity2);
        const compareResult2 = service.compareGestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGestion(entity1, entity2);
        const compareResult2 = service.compareGestion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
