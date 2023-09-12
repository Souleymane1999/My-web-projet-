import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../agent-structure.test-samples';

import { AgentStructureFormService } from './agent-structure-form.service';

describe('AgentStructure Form Service', () => {
  let service: AgentStructureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgentStructureFormService);
  });

  describe('Service methods', () => {
    describe('createAgentStructureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAgentStructureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            struture: expect.any(Object),
            agent: expect.any(Object),
          })
        );
      });

      it('passing IAgentStructure should create a new form with FormGroup', () => {
        const formGroup = service.createAgentStructureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            struture: expect.any(Object),
            agent: expect.any(Object),
          })
        );
      });
    });

    describe('getAgentStructure', () => {
      it('should return NewAgentStructure for default AgentStructure initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAgentStructureFormGroup(sampleWithNewData);

        const agentStructure = service.getAgentStructure(formGroup) as any;

        expect(agentStructure).toMatchObject(sampleWithNewData);
      });

      it('should return NewAgentStructure for empty AgentStructure initial value', () => {
        const formGroup = service.createAgentStructureFormGroup();

        const agentStructure = service.getAgentStructure(formGroup) as any;

        expect(agentStructure).toMatchObject({});
      });

      it('should return IAgentStructure', () => {
        const formGroup = service.createAgentStructureFormGroup(sampleWithRequiredData);

        const agentStructure = service.getAgentStructure(formGroup) as any;

        expect(agentStructure).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAgentStructure should not enable id FormControl', () => {
        const formGroup = service.createAgentStructureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAgentStructure should disable id FormControl', () => {
        const formGroup = service.createAgentStructureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
