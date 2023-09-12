import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GestionFormService } from './gestion-form.service';
import { GestionService } from '../service/gestion.service';
import { IGestion } from '../gestion.model';

import { GestionUpdateComponent } from './gestion-update.component';

describe('Gestion Management Update Component', () => {
  let comp: GestionUpdateComponent;
  let fixture: ComponentFixture<GestionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let gestionFormService: GestionFormService;
  let gestionService: GestionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), GestionUpdateComponent],
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
      .overrideTemplate(GestionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GestionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    gestionFormService = TestBed.inject(GestionFormService);
    gestionService = TestBed.inject(GestionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const gestion: IGestion = { id: 456 };

      activatedRoute.data = of({ gestion });
      comp.ngOnInit();

      expect(comp.gestion).toEqual(gestion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestion>>();
      const gestion = { id: 123 };
      jest.spyOn(gestionFormService, 'getGestion').mockReturnValue(gestion);
      jest.spyOn(gestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gestion }));
      saveSubject.complete();

      // THEN
      expect(gestionFormService.getGestion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(gestionService.update).toHaveBeenCalledWith(expect.objectContaining(gestion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestion>>();
      const gestion = { id: 123 };
      jest.spyOn(gestionFormService, 'getGestion').mockReturnValue({ id: null });
      jest.spyOn(gestionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: gestion }));
      saveSubject.complete();

      // THEN
      expect(gestionFormService.getGestion).toHaveBeenCalled();
      expect(gestionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IGestion>>();
      const gestion = { id: 123 };
      jest.spyOn(gestionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ gestion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(gestionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
