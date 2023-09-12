import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITransfert } from '../transfert.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../transfert.test-samples';

import { TransfertService, RestTransfert } from './transfert.service';

const requireRestSample: RestTransfert = {
  ...sampleWithRequiredData,
  dateTransfert: sampleWithRequiredData.dateTransfert?.toJSON(),
};

describe('Transfert Service', () => {
  let service: TransfertService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransfert | ITransfert[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransfertService);
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

    it('should create a Transfert', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const transfert = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transfert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Transfert', () => {
      const transfert = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transfert).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Transfert', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Transfert', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Transfert', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTransfertToCollectionIfMissing', () => {
      it('should add a Transfert to an empty array', () => {
        const transfert: ITransfert = sampleWithRequiredData;
        expectedResult = service.addTransfertToCollectionIfMissing([], transfert);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transfert);
      });

      it('should not add a Transfert to an array that contains it', () => {
        const transfert: ITransfert = sampleWithRequiredData;
        const transfertCollection: ITransfert[] = [
          {
            ...transfert,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransfertToCollectionIfMissing(transfertCollection, transfert);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Transfert to an array that doesn't contain it", () => {
        const transfert: ITransfert = sampleWithRequiredData;
        const transfertCollection: ITransfert[] = [sampleWithPartialData];
        expectedResult = service.addTransfertToCollectionIfMissing(transfertCollection, transfert);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transfert);
      });

      it('should add only unique Transfert to an array', () => {
        const transfertArray: ITransfert[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transfertCollection: ITransfert[] = [sampleWithRequiredData];
        expectedResult = service.addTransfertToCollectionIfMissing(transfertCollection, ...transfertArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transfert: ITransfert = sampleWithRequiredData;
        const transfert2: ITransfert = sampleWithPartialData;
        expectedResult = service.addTransfertToCollectionIfMissing([], transfert, transfert2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transfert);
        expect(expectedResult).toContain(transfert2);
      });

      it('should accept null and undefined values', () => {
        const transfert: ITransfert = sampleWithRequiredData;
        expectedResult = service.addTransfertToCollectionIfMissing([], null, transfert, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transfert);
      });

      it('should return initial array if no Transfert is added', () => {
        const transfertCollection: ITransfert[] = [sampleWithRequiredData];
        expectedResult = service.addTransfertToCollectionIfMissing(transfertCollection, undefined, null);
        expect(expectedResult).toEqual(transfertCollection);
      });
    });

    describe('compareTransfert', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransfert(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTransfert(entity1, entity2);
        const compareResult2 = service.compareTransfert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTransfert(entity1, entity2);
        const compareResult2 = service.compareTransfert(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTransfert(entity1, entity2);
        const compareResult2 = service.compareTransfert(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
