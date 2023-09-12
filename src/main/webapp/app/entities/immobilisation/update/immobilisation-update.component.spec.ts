import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ImmobilisationFormService } from './immobilisation-form.service';
import { ImmobilisationService } from '../service/immobilisation.service';
import { IImmobilisation } from '../immobilisation.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { IGestion } from 'app/entities/gestion/gestion.model';
import { GestionService } from 'app/entities/gestion/service/gestion.service';

import { ImmobilisationUpdateComponent } from './immobilisation-update.component';

describe('Immobilisation Management Update Component', () => {
  let comp: ImmobilisationUpdateComponent;
  let fixture: ComponentFixture<ImmobilisationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let immobilisationFormService: ImmobilisationFormService;
  let immobilisationService: ImmobilisationService;
  let categorieService: CategorieService;
  let gestionService: GestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ImmobilisationUpdateComponent],
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
      .overrideTemplate(ImmobilisationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImmobilisationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    immobilisationFormService = TestBed.inject(ImmobilisationFormService);
    immobilisationService = TestBed.inject(ImmobilisationService);
    categorieService = TestBed.inject(CategorieService);
    gestionService = TestBed.inject(GestionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categorie query and add missing value', () => {
      const immobilisation: IImmobilisation = { id: 456 };
      const categorie: ICategorie = { id: 4880 };
      immobilisation.categorie = categorie;

      const categorieCollection: ICategorie[] = [{ id: 93268 }];
      jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
      const additionalCategories = [categorie];
      const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
      jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ immobilisation });
      comp.ngOnInit();

      expect(categorieService.query).toHaveBeenCalled();
      expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(
        categorieCollection,
        ...additionalCategories.map(expect.objectContaining)
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Gestion query and add missing value', () => {
      const immobilisation: IImmobilisation = { id: 456 };
      const gestion: IGestion = { id: 6580 };
      immobilisation.gestion = gestion;

      const gestionCollection: IGestion[] = [{ id: 41603 }];
      jest.spyOn(gestionService, 'query').mockReturnValue(of(new HttpResponse({ body: gestionCollection })));
      const additionalGestions = [gestion];
      const expectedCollection: IGestion[] = [...additionalGestions, ...gestionCollection];
      jest.spyOn(gestionService, 'addGestionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ immobilisation });
      comp.ngOnInit();

      expect(gestionService.query).toHaveBeenCalled();
      expect(gestionService.addGestionToCollectionIfMissing).toHaveBeenCalledWith(
        gestionCollection,
        ...additionalGestions.map(expect.objectContaining)
      );
      expect(comp.gestionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const immobilisation: IImmobilisation = { id: 456 };
      const categorie: ICategorie = { id: 95200 };
      immobilisation.categorie = categorie;
      const gestion: IGestion = { id: 67257 };
      immobilisation.gestion = gestion;

      activatedRoute.data = of({ immobilisation });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(categorie);
      expect(comp.gestionsSharedCollection).toContain(gestion);
      expect(comp.immobilisation).toEqual(immobilisation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImmobilisation>>();
      const immobilisation = { id: 123 };
      jest.spyOn(immobilisationFormService, 'getImmobilisation').mockReturnValue(immobilisation);
      jest.spyOn(immobilisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ immobilisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: immobilisation }));
      saveSubject.complete();

      // THEN
      expect(immobilisationFormService.getImmobilisation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(immobilisationService.update).toHaveBeenCalledWith(expect.objectContaining(immobilisation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImmobilisation>>();
      const immobilisation = { id: 123 };
      jest.spyOn(immobilisationFormService, 'getImmobilisation').mockReturnValue({ id: null });
      jest.spyOn(immobilisationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ immobilisation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: immobilisation }));
      saveSubject.complete();

      // THEN
      expect(immobilisationFormService.getImmobilisation).toHaveBeenCalled();
      expect(immobilisationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImmobilisation>>();
      const immobilisation = { id: 123 };
      jest.spyOn(immobilisationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ immobilisation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(immobilisationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategorie', () => {
      it('Should forward to categorieService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categorieService, 'compareCategorie');
        comp.compareCategorie(entity, entity2);
        expect(categorieService.compareCategorie).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGestion', () => {
      it('Should forward to gestionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gestionService, 'compareGestion');
        comp.compareGestion(entity, entity2);
        expect(gestionService.compareGestion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
