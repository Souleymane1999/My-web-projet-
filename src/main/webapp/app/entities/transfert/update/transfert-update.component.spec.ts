import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TransfertFormService } from './transfert-form.service';
import { TransfertService } from '../service/transfert.service';
import { ITransfert } from '../transfert.model';
import { IImmobilisation } from 'app/entities/immobilisation/immobilisation.model';
import { ImmobilisationService } from 'app/entities/immobilisation/service/immobilisation.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

import { TransfertUpdateComponent } from './transfert-update.component';

describe('Transfert Management Update Component', () => {
  let comp: TransfertUpdateComponent;
  let fixture: ComponentFixture<TransfertUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let transfertFormService: TransfertFormService;
  let transfertService: TransfertService;
  let immobilisationService: ImmobilisationService;
  let structureService: StructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TransfertUpdateComponent],
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
      .overrideTemplate(TransfertUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TransfertUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transfertFormService = TestBed.inject(TransfertFormService);
    transfertService = TestBed.inject(TransfertService);
    immobilisationService = TestBed.inject(ImmobilisationService);
    structureService = TestBed.inject(StructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Immobilisation query and add missing value', () => {
      const transfert: ITransfert = { id: 456 };
      const immobilisation: IImmobilisation = { id: 41736 };
      transfert.immobilisation = immobilisation;

      const immobilisationCollection: IImmobilisation[] = [{ id: 60781 }];
      jest.spyOn(immobilisationService, 'query').mockReturnValue(of(new HttpResponse({ body: immobilisationCollection })));
      const additionalImmobilisations = [immobilisation];
      const expectedCollection: IImmobilisation[] = [...additionalImmobilisations, ...immobilisationCollection];
      jest.spyOn(immobilisationService, 'addImmobilisationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      expect(immobilisationService.query).toHaveBeenCalled();
      expect(immobilisationService.addImmobilisationToCollectionIfMissing).toHaveBeenCalledWith(
        immobilisationCollection,
        ...additionalImmobilisations.map(expect.objectContaining)
      );
      expect(comp.immobilisationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Structure query and add missing value', () => {
      const transfert: ITransfert = { id: 456 };
      const struture: IStructure = { id: 40171 };
      transfert.struture = struture;

      const structureCollection: IStructure[] = [{ id: 86022 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [struture];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(
        structureCollection,
        ...additionalStructures.map(expect.objectContaining)
      );
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const transfert: ITransfert = { id: 456 };
      const immobilisation: IImmobilisation = { id: 27294 };
      transfert.immobilisation = immobilisation;
      const struture: IStructure = { id: 26007 };
      transfert.struture = struture;

      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      expect(comp.immobilisationsSharedCollection).toContain(immobilisation);
      expect(comp.structuresSharedCollection).toContain(struture);
      expect(comp.transfert).toEqual(transfert);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransfert>>();
      const transfert = { id: 123 };
      jest.spyOn(transfertFormService, 'getTransfert').mockReturnValue(transfert);
      jest.spyOn(transfertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transfert }));
      saveSubject.complete();

      // THEN
      expect(transfertFormService.getTransfert).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transfertService.update).toHaveBeenCalledWith(expect.objectContaining(transfert));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransfert>>();
      const transfert = { id: 123 };
      jest.spyOn(transfertFormService, 'getTransfert').mockReturnValue({ id: null });
      jest.spyOn(transfertService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfert: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: transfert }));
      saveSubject.complete();

      // THEN
      expect(transfertFormService.getTransfert).toHaveBeenCalled();
      expect(transfertService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITransfert>>();
      const transfert = { id: 123 };
      jest.spyOn(transfertService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transfertService.update).toHaveBeenCalled();
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

    describe('compareStructure', () => {
      it('Should forward to structureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(structureService, 'compareStructure');
        comp.compareStructure(entity, entity2);
        expect(structureService.compareStructure).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
