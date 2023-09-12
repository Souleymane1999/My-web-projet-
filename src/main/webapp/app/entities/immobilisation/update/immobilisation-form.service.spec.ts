import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../immobilisation.test-samples';

import { ImmobilisationFormService } from './immobilisation-form.service';

describe('Immobilisation Form Service', () => {
  let service: ImmobilisationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImmobilisationFormService);
  });

  describe('Service methods', () => {
    describe('createImmobilisationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImmobilisationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            valeur: expect.any(Object),
            etat: expect.any(Object),
            quantite: expect.any(Object),
            dateAcquisition: expect.any(Object),
            typeAmortissement: expect.any(Object),
            dureeAmortissement: expect.any(Object),
            categorie: expect.any(Object),
            gestion: expect.any(Object),
          })
        );
      });

      it('passing IImmobilisation should create a new form with FormGroup', () => {
        const formGroup = service.createImmobilisationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nom: expect.any(Object),
            description: expect.any(Object),
            valeur: expect.any(Object),
            etat: expect.any(Object),
            quantite: expect.any(Object),
            dateAcquisition: expect.any(Object),
            typeAmortissement: expect.any(Object),
            dureeAmortissement: expect.any(Object),
            categorie: expect.any(Object),
            gestion: expect.any(Object),
          })
        );
      });
    });

    describe('getImmobilisation', () => {
      it('should return NewImmobilisation for default Immobilisation initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createImmobilisationFormGroup(sampleWithNewData);

        const immobilisation = service.getImmobilisation(formGroup) as any;

        expect(immobilisation).toMatchObject(sampleWithNewData);
      });

      it('should return NewImmobilisation for empty Immobilisation initial value', () => {
        const formGroup = service.createImmobilisationFormGroup();

        const immobilisation = service.getImmobilisation(formGroup) as any;

        expect(immobilisation).toMatchObject({});
      });

      it('should return IImmobilisation', () => {
        const formGroup = service.createImmobilisationFormGroup(sampleWithRequiredData);

        const immobilisation = service.getImmobilisation(formGroup) as any;

        expect(immobilisation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImmobilisation should not enable id FormControl', () => {
        const formGroup = service.createImmobilisationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImmobilisation should disable id FormControl', () => {
        const formGroup = service.createImmobilisationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
