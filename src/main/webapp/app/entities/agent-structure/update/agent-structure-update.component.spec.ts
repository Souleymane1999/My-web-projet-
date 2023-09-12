import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AgentStructureFormService } from './agent-structure-form.service';
import { AgentStructureService } from '../service/agent-structure.service';
import { IAgentStructure } from '../agent-structure.model';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';
import { IAgentAffecter } from 'app/entities/agent-affecter/agent-affecter.model';
import { AgentAffecterService } from 'app/entities/agent-affecter/service/agent-affecter.service';

import { AgentStructureUpdateComponent } from './agent-structure-update.component';

describe('AgentStructure Management Update Component', () => {
  let comp: AgentStructureUpdateComponent;
  let fixture: ComponentFixture<AgentStructureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agentStructureFormService: AgentStructureFormService;
  let agentStructureService: AgentStructureService;
  let structureService: StructureService;
  let agentAffecterService: AgentAffecterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AgentStructureUpdateComponent],
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
      .overrideTemplate(AgentStructureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgentStructureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agentStructureFormService = TestBed.inject(AgentStructureFormService);
    agentStructureService = TestBed.inject(AgentStructureService);
    structureService = TestBed.inject(StructureService);
    agentAffecterService = TestBed.inject(AgentAffecterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Structure query and add missing value', () => {
      const agentStructure: IAgentStructure = { id: 456 };
      const struture: IStructure = { id: 49929 };
      agentStructure.struture = struture;

      const structureCollection: IStructure[] = [{ id: 76138 }];
      jest.spyOn(structureService, 'query').mockReturnValue(of(new HttpResponse({ body: structureCollection })));
      const additionalStructures = [struture];
      const expectedCollection: IStructure[] = [...additionalStructures, ...structureCollection];
      jest.spyOn(structureService, 'addStructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agentStructure });
      comp.ngOnInit();

      expect(structureService.query).toHaveBeenCalled();
      expect(structureService.addStructureToCollectionIfMissing).toHaveBeenCalledWith(
        structureCollection,
        ...additionalStructures.map(expect.objectContaining)
      );
      expect(comp.structuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AgentAffecter query and add missing value', () => {
      const agentStructure: IAgentStructure = { id: 456 };
      const agent: IAgentAffecter = { id: 72035 };
      agentStructure.agent = agent;

      const agentAffecterCollection: IAgentAffecter[] = [{ id: 63125 }];
      jest.spyOn(agentAffecterService, 'query').mockReturnValue(of(new HttpResponse({ body: agentAffecterCollection })));
      const additionalAgentAffecters = [agent];
      const expectedCollection: IAgentAffecter[] = [...additionalAgentAffecters, ...agentAffecterCollection];
      jest.spyOn(agentAffecterService, 'addAgentAffecterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agentStructure });
      comp.ngOnInit();

      expect(agentAffecterService.query).toHaveBeenCalled();
      expect(agentAffecterService.addAgentAffecterToCollectionIfMissing).toHaveBeenCalledWith(
        agentAffecterCollection,
        ...additionalAgentAffecters.map(expect.objectContaining)
      );
      expect(comp.agentAffectersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agentStructure: IAgentStructure = { id: 456 };
      const struture: IStructure = { id: 55447 };
      agentStructure.struture = struture;
      const agent: IAgentAffecter = { id: 39998 };
      agentStructure.agent = agent;

      activatedRoute.data = of({ agentStructure });
      comp.ngOnInit();

      expect(comp.structuresSharedCollection).toContain(struture);
      expect(comp.agentAffectersSharedCollection).toContain(agent);
      expect(comp.agentStructure).toEqual(agentStructure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgentStructure>>();
      const agentStructure = { id: 123 };
      jest.spyOn(agentStructureFormService, 'getAgentStructure').mockReturnValue(agentStructure);
      jest.spyOn(agentStructureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentStructure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agentStructure }));
      saveSubject.complete();

      // THEN
      expect(agentStructureFormService.getAgentStructure).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agentStructureService.update).toHaveBeenCalledWith(expect.objectContaining(agentStructure));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgentStructure>>();
      const agentStructure = { id: 123 };
      jest.spyOn(agentStructureFormService, 'getAgentStructure').mockReturnValue({ id: null });
      jest.spyOn(agentStructureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentStructure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agentStructure }));
      saveSubject.complete();

      // THEN
      expect(agentStructureFormService.getAgentStructure).toHaveBeenCalled();
      expect(agentStructureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgentStructure>>();
      const agentStructure = { id: 123 };
      jest.spyOn(agentStructureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agentStructure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agentStructureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStructure', () => {
      it('Should forward to structureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(structureService, 'compareStructure');
        comp.compareStructure(entity, entity2);
        expect(structureService.compareStructure).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAgentAffecter', () => {
      it('Should forward to agentAffecterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(agentAffecterService, 'compareAgentAffecter');
        comp.compareAgentAffecter(entity, entity2);
        expect(agentAffecterService.compareAgentAffecter).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
