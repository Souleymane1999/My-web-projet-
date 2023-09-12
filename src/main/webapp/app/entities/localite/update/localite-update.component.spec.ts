import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LocaliteFormService } from './localite-form.service';
import { LocaliteService } from '../service/localite.service';
import { ILocalite } from '../localite.model';

import { LocaliteUpdateComponent } from './localite-update.component';

describe('Localite Management Update Component', () => {
  let comp: LocaliteUpdateComponent;
  let fixture: ComponentFixture<LocaliteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let localiteFormService: LocaliteFormService;
  let localiteService: LocaliteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), LocaliteUpdateComponent],
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
      .overrideTemplate(LocaliteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocaliteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    localiteFormService = TestBed.inject(LocaliteFormService);
    localiteService = TestBed.inject(LocaliteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const localite: ILocalite = { id: 456 };

      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      expect(comp.localite).toEqual(localite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocalite>>();
      const localite = { id: 123 };
      jest.spyOn(localiteFormService, 'getLocalite').mockReturnValue(localite);
      jest.spyOn(localiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localite }));
      saveSubject.complete();

      // THEN
      expect(localiteFormService.getLocalite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(localiteService.update).toHaveBeenCalledWith(expect.objectContaining(localite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocalite>>();
      const localite = { id: 123 };
      jest.spyOn(localiteFormService, 'getLocalite').mockReturnValue({ id: null });
      jest.spyOn(localiteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: localite }));
      saveSubject.complete();

      // THEN
      expect(localiteFormService.getLocalite).toHaveBeenCalled();
      expect(localiteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocalite>>();
      const localite = { id: 123 };
      jest.spyOn(localiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ localite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(localiteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
