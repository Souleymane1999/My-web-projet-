import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAgentStructure } from '../agent-structure.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../agent-structure.test-samples';

import { AgentStructureService } from './agent-structure.service';

const requireRestSample: IAgentStructure = {
  ...sampleWithRequiredData,
};

describe('AgentStructure Service', () => {
  let service: AgentStructureService;
  let httpMock: HttpTestingController;
  let expectedResult: IAgentStructure | IAgentStructure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AgentStructureService);
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

    it('should create a AgentStructure', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const agentStructure = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(agentStructure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AgentStructure', () => {
      const agentStructure = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(agentStructure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AgentStructure', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AgentStructure', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AgentStructure', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAgentStructureToCollectionIfMissing', () => {
      it('should add a AgentStructure to an empty array', () => {
        const agentStructure: IAgentStructure = sampleWithRequiredData;
        expectedResult = service.addAgentStructureToCollectionIfMissing([], agentStructure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agentStructure);
      });

      it('should not add a AgentStructure to an array that contains it', () => {
        const agentStructure: IAgentStructure = sampleWithRequiredData;
        const agentStructureCollection: IAgentStructure[] = [
          {
            ...agentStructure,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAgentStructureToCollectionIfMissing(agentStructureCollection, agentStructure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AgentStructure to an array that doesn't contain it", () => {
        const agentStructure: IAgentStructure = sampleWithRequiredData;
        const agentStructureCollection: IAgentStructure[] = [sampleWithPartialData];
        expectedResult = service.addAgentStructureToCollectionIfMissing(agentStructureCollection, agentStructure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agentStructure);
      });

      it('should add only unique AgentStructure to an array', () => {
        const agentStructureArray: IAgentStructure[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const agentStructureCollection: IAgentStructure[] = [sampleWithRequiredData];
        expectedResult = service.addAgentStructureToCollectionIfMissing(agentStructureCollection, ...agentStructureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const agentStructure: IAgentStructure = sampleWithRequiredData;
        const agentStructure2: IAgentStructure = sampleWithPartialData;
        expectedResult = service.addAgentStructureToCollectionIfMissing([], agentStructure, agentStructure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(agentStructure);
        expect(expectedResult).toContain(agentStructure2);
      });

      it('should accept null and undefined values', () => {
        const agentStructure: IAgentStructure = sampleWithRequiredData;
        expectedResult = service.addAgentStructureToCollectionIfMissing([], null, agentStructure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(agentStructure);
      });

      it('should return initial array if no AgentStructure is added', () => {
        const agentStructureCollection: IAgentStructure[] = [sampleWithRequiredData];
        expectedResult = service.addAgentStructureToCollectionIfMissing(agentStructureCollection, undefined, null);
        expect(expectedResult).toEqual(agentStructureCollection);
      });
    });

    describe('compareAgentStructure', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAgentStructure(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAgentStructure(entity1, entity2);
        const compareResult2 = service.compareAgentStructure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAgentStructure(entity1, entity2);
        const compareResult2 = service.compareAgentStructure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAgentStructure(entity1, entity2);
        const compareResult2 = service.compareAgentStructure(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
