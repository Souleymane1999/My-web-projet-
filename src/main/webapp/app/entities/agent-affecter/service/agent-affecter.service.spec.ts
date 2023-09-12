import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAgentAffecter } from '../agent-affecter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../agent-affecter.test-samples';

import { AgentAffecterService } from './agent-affecter.service';

const requireRestSample: IAgentAffecter = {
  ...sampleWithRequiredData,
};

describe('AgentAffecter Service', () => {
  let service: AgentAffecterService;
  let httpMock: HttpTestingController;
  let expectedResult: IAgentAffecter | IAgentAffecter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgentAffecterService);
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

    it('should create a AgentAffecter', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const agentAffecter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(agentAffecter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgentAffecter', () => {
      const agentAffecter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(agentAffecter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AgentAffecter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AgentAffecter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AgentAffecter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAgentAffecterToCollectionIfMissing', () => {
      it('should add a AgentAffecter to an empty array', () => {
        const agentAffecter: IAgentAffecter = sampleWithRequiredData;
        expectedResult = service.addAgentAffecterToCollectionIfMissing([], agentAffecter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agentAffecter);
      });

      it('should not add a AgentAffecter to an array that contains it', () => {
        const agentAffecter: IAgentAffecter = sampleWithRequiredData;
        const agentAffecterCollection: IAgentAffecter[] = [
          {
            ...agentAffecter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAgentAffecterToCollectionIfMissing(agentAffecterCollection, agentAffecter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgentAffecter to an array that doesn't contain it", () => {
        const agentAffecter: IAgentAffecter = sampleWithRequiredData;
        const agentAffecterCollection: IAgentAffecter[] = [sampleWithPartialData];
        expectedResult = service.addAgentAffecterToCollectionIfMissing(agentAffecterCollection, agentAffecter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agentAffecter);
      });

      it('should add only unique AgentAffecter to an array', () => {
        const agentAffecterArray: IAgentAffecter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const agentAffecterCollection: IAgentAffecter[] = [sampleWithRequiredData];
        expectedResult = service.addAgentAffecterToCollectionIfMissing(agentAffecterCollection, ...agentAffecterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agentAffecter: IAgentAffecter = sampleWithRequiredData;
        const agentAffecter2: IAgentAffecter = sampleWithPartialData;
        expectedResult = service.addAgentAffecterToCollectionIfMissing([], agentAffecter, agentAffecter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agentAffecter);
        expect(expectedResult).toContain(agentAffecter2);
      });

      it('should accept null and undefined values', () => {
        const agentAffecter: IAgentAffecter = sampleWithRequiredData;
        expectedResult = service.addAgentAffecterToCollectionIfMissing([], null, agentAffecter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agentAffecter);
      });

      it('should return initial array if no AgentAffecter is added', () => {
        const agentAffecterCollection: IAgentAffecter[] = [sampleWithRequiredData];
        expectedResult = service.addAgentAffecterToCollectionIfMissing(agentAffecterCollection, undefined, null);
        expect(expectedResult).toEqual(agentAffecterCollection);
      });
    });

    describe('compareAgentAffecter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAgentAffecter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAgentAffecter(entity1, entity2);
        const compareResult2 = service.compareAgentAffecter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAgentAffecter(entity1, entity2);
        const compareResult2 = service.compareAgentAffecter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAgentAffecter(entity1, entity2);
        const compareResult2 = service.compareAgentAffecter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
