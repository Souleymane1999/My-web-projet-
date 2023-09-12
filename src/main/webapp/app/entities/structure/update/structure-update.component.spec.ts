import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StructureFormService } from './structure-form.service';
import { StructureService } from '../service/structure.service';
import { IStructure } from '../structure.model';
import { ILocalite } from 'app/entities/localite/localite.model';
import { LocaliteService } from 'app/entities/localite/service/localite.service';

import { StructureUpdateComponent } from './structure-update.component';

describe('Structure Management Update Component', () => {
  let comp: StructureUpdateComponent;
  let fixture: ComponentFixture<StructureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let structureFormService: StructureFormService;
  let structureService: StructureService;
  let localiteService: LocaliteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StructureUpdateComponent],
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
      .overrideTemplate(StructureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StructureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    structureFormService = TestBed.inject(StructureFormService);
    structureService = TestBed.inject(StructureService);
    localiteService = TestBed.inject(LocaliteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Localite query and add missing value', () => {
      const structure: IStructure = { id: 456 };
      const localite: ILocalite = { id: 89774 };
      structure.localite = localite;

      const localiteCollection: ILocalite[] = [{ id: 80102 }];
      jest.spyOn(localiteService, 'query').mockReturnValue(of(new HttpResponse({ body: localiteCollection })));
      const additionalLocalites = [localite];
      const expectedCollection: ILocalite[] = [...additionalLocalites, ...localiteCollection];
      jest.spyOn(localiteService, 'addLocaliteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ structure });
      comp.ngOnInit();

      expect(localiteService.query).toHaveBeenCalled();
      expect(localiteService.addLocaliteToCollectionIfMissing).toHaveBeenCalledWith(
        localiteCollection,
        ...additionalLocalites.map(expect.objectContaining)
      );
      expect(comp.localitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const structure: IStructure = { id: 456 };
      const localite: ILocalite = { id: 82401 };
      structure.localite = localite;

      activatedRoute.data = of({ structure });
      comp.ngOnInit();

      expect(comp.localitesSharedCollection).toContain(localite);
      expect(comp.structure).toEqual(structure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStructure>>();
      const structure = { id: 123 };
      jest.spyOn(structureFormService, 'getStructure').mockReturnValue(structure);
      jest.spyOn(structureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ structure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: structure }));
      saveSubject.complete();

      // THEN
      expect(structureFormService.getStructure).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(structureService.update).toHaveBeenCalledWith(expect.objectContaining(structure));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStructure>>();
      const structure = { id: 123 };
      jest.spyOn(structureFormService, 'getStructure').mockReturnValue({ id: null });
      jest.spyOn(structureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ structure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: structure }));
      saveSubject.complete();

      // THEN
      expect(structureFormService.getStructure).toHaveBeenCalled();
      expect(structureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStructure>>();
      const structure = { id: 123 };
      jest.spyOn(structureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ structure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(structureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareLocalite', () => {
      it('Should forward to localiteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(localiteService, 'compareLocalite');
        comp.compareLocalite(entity, entity2);
        expect(localiteService.compareLocalite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
