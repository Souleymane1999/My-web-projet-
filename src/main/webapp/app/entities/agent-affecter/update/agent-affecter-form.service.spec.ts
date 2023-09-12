import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../agent-affecter.test-samples';

import { AgentAffecterFormService } from './agent-affecter-form.service';

describe('AgentAffecter Form Service', () => {
  let service: AgentAffecterFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AgentAffecterFormService);
  });

  describe('Service methods', () => {
    describe('createAgentAffecterFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAgentAffecterFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matricule: expect.any(Object),
            nomUtilisateur: expect.any(Object),
            prenomUtilisateur: expect.any(Object),
            poste: expect.any(Object),
            adresse: expect.any(Object),
            telephone: expect.any(Object),
          })
        );
      });

      it('passing IAgentAffecter should create a new form with FormGroup', () => {
        const formGroup = service.createAgentAffecterFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matricule: expect.any(Object),
            nomUtilisateur: expect.any(Object),
            prenomUtilisateur: expect.any(Object),
            poste: expect.any(Object),
            adresse: expect.any(Object),
            telephone: expect.any(Object),
          })
        );
      });
    });

    describe('getAgentAffecter', () => {
      it('should return NewAgentAffecter for default AgentAffecter initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAgentAffecterFormGroup(sampleWithNewData);

        const agentAffecter = service.getAgentAffecter(formGroup) as any;

        expect(agentAffecter).toMatchObject(sampleWithNewData);
      });

      it('should return NewAgentAffecter for empty AgentAffecter initial value', () => {
        const formGroup = service.createAgentAffecterFormGroup();

        const agentAffecter = service.getAgentAffecter(formGroup) as any;

        expect(agentAffecter).toMatchObject({});
      });

      it('should return IAgentAffecter', () => {
        const formGroup = service.createAgentAffecterFormGroup(sampleWithRequiredData);

        const agentAffecter = service.getAgentAffecter(formGroup) as any;

        expect(agentAffecter).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAgentAffecter should not enable id FormControl', () => {
        const formGroup = service.createAgentAffecterFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAgentAffecter should disable id FormControl', () => {
        const formGroup = service.createAgentAffecterFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
