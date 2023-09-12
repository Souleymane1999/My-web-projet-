import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AgentAffecterFormService } from './agent-affecter-form.service';
import { AgentAffecterService } from '../service/agent-affecter.service';
import { IAgentAffecter } from '../agent-affecter.model';

import { AgentAffecterUpdateComponent } from './agent-affecter-update.component';

describe('AgentAffecter Management Update Component', () => {
  let comp: AgentAffecterUpdateComponent;
  let fixture: ComponentFixture<AgentAffecterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agentAffecterFormService: AgentAffecterFormService;
  let agentAffecterService: AgentAffecterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AgentAffecterUpdateComponent],
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
      .overrideTemplate(AgentAffecterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgentAffecterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agentAffecterFormService = TestBed.inject(AgentAffecterFormService);
    agentAffecterService = TestBed.inject(AgentAffecterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const agentAffecter: IAgentAffecter = { id: 456 };

      activatedRoute.data = of({ agentAffecter });
      comp.ngOnInit();

      expect(comp.agentAffecter).toEqual(agentAffecter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgentAffecter>>();
      const agentAffecter = { id: 123 };
      jest.spyOn(agentAffecterFormService, 'getAgentAffecter').mockReturnValue(agentAffecter);
      jest.spyOn(agentAffecterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentAffecter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agentAffecter }));
      saveSubject.complete();

      // THEN
      expect(agentAffecterFormService.getAgentAffecter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agentAffecterService.update).toHaveBeenCalledWith(expect.objectContaining(agentAffecter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgentAffecter>>();
      const agentAffecter = { id: 123 };
      jest.spyOn(agentAffecterFormService, 'getAgentAffecter').mockReturnValue({ id: null });
      jest.spyOn(agentAffecterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentAffecter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agentAffecter }));
      saveSubject.complete();

      // THEN
      expect(agentAffecterFormService.getAgentAffecter).toHaveBeenCalled();
      expect(agentAffecterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgentAffecter>>();
      const agentAffecter = { id: 123 };
      jest.spyOn(agentAffecterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentAffecter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agentAffecterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
