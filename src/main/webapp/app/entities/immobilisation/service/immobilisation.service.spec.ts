import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IImmobilisation } from '../immobilisation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../immobilisation.test-samples';

import { ImmobilisationService, RestImmobilisation } from './immobilisation.service';

const requireRestSample: RestImmobilisation = {
  ...sampleWithRequiredData,
  dateAcquisition: sampleWithRequiredData.dateAcquisition?.toJSON(),
};

describe('Immobilisation Service', () => {
  let service: ImmobilisationService;
  let httpMock: HttpTestingController;
  let expectedResult: IImmobilisation | IImmobilisation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ImmobilisationService);
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

    it('should create a Immobilisation', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const immobilisation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(immobilisation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Immobilisation', () => {
      const immobilisation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(immobilisation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Immobilisation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Immobilisation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Immobilisation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addImmobilisationToCollectionIfMissing', () => {
      it('should add a Immobilisation to an empty array', () => {
        const immobilisation: IImmobilisation = sampleWithRequiredData;
        expectedResult = service.addImmobilisationToCollectionIfMissing([], immobilisation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(immobilisation);
      });

      it('should not add a Immobilisation to an array that contains it', () => {
        const immobilisation: IImmobilisation = sampleWithRequiredData;
        const immobilisationCollection: IImmobilisation[] = [
          {
            ...immobilisation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImmobilisationToCollectionIfMissing(immobilisationCollection, immobilisation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Immobilisation to an array that doesn't contain it", () => {
        const immobilisation: IImmobilisation = sampleWithRequiredData;
        const immobilisationCollection: IImmobilisation[] = [sampleWithPartialData];
        expectedResult = service.addImmobilisationToCollectionIfMissing(immobilisationCollection, immobilisation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(immobilisation);
      });

      it('should add only unique Immobilisation to an array', () => {
        const immobilisationArray: IImmobilisation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const immobilisationCollection: IImmobilisation[] = [sampleWithRequiredData];
        expectedResult = service.addImmobilisationToCollectionIfMissing(immobilisationCollection, ...immobilisationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const immobilisation: IImmobilisation = sampleWithRequiredData;
        const immobilisation2: IImmobilisation = sampleWithPartialData;
        expectedResult = service.addImmobilisationToCollectionIfMissing([], immobilisation, immobilisation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(immobilisation);
        expect(expectedResult).toContain(immobilisation2);
      });

      it('should accept null and undefined values', () => {
        const immobilisation: IImmobilisation = sampleWithRequiredData;
        expectedResult = service.addImmobilisationToCollectionIfMissing([], null, immobilisation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(immobilisation);
      });

      it('should return initial array if no Immobilisation is added', () => {
        const immobilisationCollection: IImmobilisation[] = [sampleWithRequiredData];
        expectedResult = service.addImmobilisationToCollectionIfMissing(immobilisationCollection, undefined, null);
        expect(expectedResult).toEqual(immobilisationCollection);
      });
    });

    describe('compareImmobilisation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImmobilisation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareImmobilisation(entity1, entity2);
        const compareResult2 = service.compareImmobilisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareImmobilisation(entity1, entity2);
        const compareResult2 = service.compareImmobilisation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareImmobilisation(entity1, entity2);
        const compareResult2 = service.compareImmobilisation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
