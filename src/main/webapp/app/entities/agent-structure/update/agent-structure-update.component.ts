import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AgentStructureFormService, AgentStructureFormGroup } from './agent-structure-form.service';
import { IAgentStructure } from '../agent-structure.model';
import { AgentStructureService } from '../service/agent-structure.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';
import { IAgentAffecter } from 'app/entities/agent-affecter/agent-affecter.model';
import { AgentAffecterService } from 'app/entities/agent-affecter/service/agent-affecter.service';

@Component({
  standalone: true,
  selector: 'jhi-agent-structure-update',
  templateUrl: './agent-structure-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgentStructureUpdateComponent implements OnInit {
  isSaving = false;
  agentStructure: IAgentStructure | null = null;

  structuresSharedCollection: IStructure[] = [];
  agentAffectersSharedCollection: IAgentAffecter[] = [];

  editForm: AgentStructureFormGroup = this.agentStructureFormService.createAgentStructureFormGroup();

  constructor(
    protected agentStructureService: AgentStructureService,
    protected agentStructureFormService: AgentStructureFormService,
    protected structureService: StructureService,
    protected agentAffecterService: AgentAffecterService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareStructure = (o1: IStructure | null, o2: IStructure | null): boolean => this.structureService.compareStructure(o1, o2);

  compareAgentAffecter = (o1: IAgentAffecter | null, o2: IAgentAffecter | null): boolean =>
    this.agentAffecterService.compareAgentAffecter(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agentStructure }) => {
      this.agentStructure = agentStructure;
      if (agentStructure) {
        this.updateForm(agentStructure);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agentStructure = this.agentStructureFormService.getAgentStructure(this.editForm);
    if (agentStructure.id !== null) {
      this.subscribeToSaveResponse(this.agentStructureService.update(agentStructure));
    } else {
      this.subscribeToSaveResponse(this.agentStructureService.create(agentStructure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgentStructure>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(agentStructure: IAgentStructure): void {
    this.agentStructure = agentStructure;
    this.agentStructureFormService.resetForm(this.editForm, agentStructure);

    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing<IStructure>(
      this.structuresSharedCollection,
      agentStructure.struture
    );
    this.agentAffectersSharedCollection = this.agentAffecterService.addAgentAffecterToCollectionIfMissing<IAgentAffecter>(
      this.agentAffectersSharedCollection,
      agentStructure.agent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.structureService
      .query()
      .pipe(map((res: HttpResponse<IStructure[]>) => res.body ?? []))
      .pipe(
        map((structures: IStructure[]) =>
          this.structureService.addStructureToCollectionIfMissing<IStructure>(structures, this.agentStructure?.struture)
        )
      )
      .subscribe((structures: IStructure[]) => (this.structuresSharedCollection = structures));

    this.agentAffecterService
      .query()
      .pipe(map((res: HttpResponse<IAgentAffecter[]>) => res.body ?? []))
      .pipe(
        map((agentAffecters: IAgentAffecter[]) =>
          this.agentAffecterService.addAgentAffecterToCollectionIfMissing<IAgentAffecter>(agentAffecters, this.agentStructure?.agent)
        )
      )
      .subscribe((agentAffecters: IAgentAffecter[]) => (this.agentAffectersSharedCollection = agentAffecters));
  }
}
