import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MaintenanceFormService } from './maintenance-form.service';
import { MaintenanceService } from '../service/maintenance.service';
import { IMaintenance } from '../maintenance.model';
import { IImmobilisation } from 'app/entities/immobilisation/immobilisation.model';
import { ImmobilisationService } from 'app/entities/immobilisation/service/immobilisation.service';

import { MaintenanceUpdateComponent } from './maintenance-update.component';

describe('Maintenance Management Update Component', () => {
  let comp: MaintenanceUpdateComponent;
  let fixture: ComponentFixture<MaintenanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let maintenanceFormService: MaintenanceFormService;
  let maintenanceService: MaintenanceService;
  let immobilisationService: ImmobilisationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MaintenanceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(MaintenanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MaintenanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maintenanceFormService = TestBed.inject(MaintenanceFormService);
    maintenanceService = TestBed.inject(MaintenanceService);
    immobilisationService = TestBed.inject(ImmobilisationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Immobilisation query and add missing value', () => {
      const maintenance: IMaintenance = { id: 456 };
      const immobilisation: IImmobilisation = { id: 16199 };
      maintenance.immobilisation = immobilisation;

      const immobilisationCollection: IImmobilisation[] = [{ id: 43331 }];
      jest.spyOn(immobilisationService, 'query').mockReturnValue(of(new HttpResponse({ body: immobilisationCollection })));
      const additionalImmobilisations = [immobilisation];
      const expectedCollection: IImmobilisation[] = [...additionalImmobilisations, ...immobilisationCollection];
      jest.spyOn(immobilisationService, 'addImmobilisationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      expect(immobilisationService.query).toHaveBeenCalled();
      expect(immobilisationService.addImmobilisationToCollectionIfMissing).toHaveBeenCalledWith(
        immobilisationCollection,
        ...additionalImmobilisations.map(expect.objectContaining)
      );
      expect(comp.immobilisationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const maintenance: IMaintenance = { id: 456 };
      const immobilisation: IImmobilisation = { id: 69257 };
      maintenance.immobilisation = immobilisation;

      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      expect(comp.immobilisationsSharedCollection).toContain(immobilisation);
      expect(comp.maintenance).toEqual(maintenance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenance>>();
      const maintenance = { id: 123 };
      jest.spyOn(maintenanceFormService, 'getMaintenance').mockReturnValue(maintenance);
      jest.spyOn(maintenanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintenance }));
      saveSubject.complete();

      // THEN
      expect(maintenanceFormService.getMaintenance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maintenanceService.update).toHaveBeenCalledWith(expect.objectContaining(maintenance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenance>>();
      const maintenance = { id: 123 };
      jest.spyOn(maintenanceFormService, 'getMaintenance').mockReturnValue({ id: null });
      jest.spyOn(maintenanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: maintenance }));
      saveSubject.complete();

      // THEN
      expect(maintenanceFormService.getMaintenance).toHaveBeenCalled();
      expect(maintenanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMaintenance>>();
      const maintenance = { id: 123 };
      jest.spyOn(maintenanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maintenanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareImmobilisation', () => {
      it('Should forward to immobilisationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(immobilisationService, 'compareImmobilisation');
        comp.compareImmobilisation(entity, entity2);
        expect(immobilisationService.compareImmobilisation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
