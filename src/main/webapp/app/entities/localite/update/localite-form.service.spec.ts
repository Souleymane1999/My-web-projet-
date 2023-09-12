import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../localite.test-samples';

import { LocaliteFormService } from './localite-form.service';

describe('Localite Form Service', () => {
  let service: LocaliteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LocaliteFormService);
  });

  describe('Service methods', () => {
    describe('createLocaliteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLocaliteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomLocalite: expect.any(Object),
            codePostal: expect.any(Object),
          })
        );
      });

      it('passing ILocalite should create a new form with FormGroup', () => {
        const formGroup = service.createLocaliteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomLocalite: expect.any(Object),
            codePostal: expect.any(Object),
          })
        );
      });
    });

    describe('getLocalite', () => {
      it('should return NewLocalite for default Localite initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createLocaliteFormGroup(sampleWithNewData);

        const localite = service.getLocalite(formGroup) as any;

        expect(localite).toMatchObject(sampleWithNewData);
      });

      it('should return NewLocalite for empty Localite initial value', () => {
        const formGroup = service.createLocaliteFormGroup();

        const localite = service.getLocalite(formGroup) as any;

        expect(localite).toMatchObject({});
      });

      it('should return ILocalite', () => {
        const formGroup = service.createLocaliteFormGroup(sampleWithRequiredData);

        const localite = service.getLocalite(formGroup) as any;

        expect(localite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILocalite should not enable id FormControl', () => {
        const formGroup = service.createLocaliteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLocalite should disable id FormControl', () => {
        const formGroup = service.createLocaliteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
