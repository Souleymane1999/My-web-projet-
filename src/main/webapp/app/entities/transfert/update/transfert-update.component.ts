import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TransfertFormService, TransfertFormGroup } from './transfert-form.service';
import { ITransfert } from '../transfert.model';
import { TransfertService } from '../service/transfert.service';
import { IImmobilisation } from 'app/entities/immobilisation/immobilisation.model';
import { ImmobilisationService } from 'app/entities/immobilisation/service/immobilisation.service';
import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { IStructure } from 'app/entities/structure/structure.model';
import { StructureService } from 'app/entities/structure/service/structure.service';

@Component({
  standalone: true,
  selector: 'jhi-transfert-update',
  templateUrl: './transfert-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransfertUpdateComponent implements OnInit {
  isSaving = false;
  transfert: ITransfert | null = null;

  immobilisationsSharedCollection: IImmobilisation[] = [];
  agentsSharedCollection: IAgent[] = [];
  structuresSharedCollection: IStructure[] = [];

  editForm: TransfertFormGroup = this.transfertFormService.createTransfertFormGroup();

  constructor(
    protected transfertService: TransfertService,
    protected transfertFormService: TransfertFormService,
    protected immobilisationService: ImmobilisationService,
    protected agentService: AgentService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImmobilisation = (o1: IImmobilisation | null, o2: IImmobilisation | null): boolean =>
    this.immobilisationService.compareImmobilisation(o1, o2);

  compareAgent = (o1: IAgent | null, o2: IAgent | null): boolean => this.agentService.compareAgent(o1, o2);

  compareStructure = (o1: IStructure | null, o2: IStructure | null): boolean => this.structureService.compareStructure(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transfert }) => {
      this.transfert = transfert;
      if (transfert) {
        this.updateForm(transfert);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transfert = this.transfertFormService.getTransfert(this.editForm);
    if (transfert.id !== null) {
      this.subscribeToSaveResponse(this.transfertService.update(transfert));
    } else {
      this.subscribeToSaveResponse(this.transfertService.create(transfert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransfert>>): void {
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

  protected updateForm(transfert: ITransfert): void {
    this.transfert = transfert;
    this.transfertFormService.resetForm(this.editForm, transfert);

    this.immobilisationsSharedCollection = this.immobilisationService.addImmobilisationToCollectionIfMissing<IImmobilisation>(
      this.immobilisationsSharedCollection,
      transfert.immobilisation
    );
    this.agentsSharedCollection = this.agentService.addAgentToCollectionIfMissing<IAgent>(this.agentsSharedCollection, transfert.agent);
    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing<IStructure>(
      this.structuresSharedCollection,
      transfert.structure
    );
  }

  protected loadRelationshipsOptions(): void {
    this.immobilisationService
      .query()
      .pipe(map((res: HttpResponse<IImmobilisation[]>) => res.body ?? []))
      .pipe(
        map((immobilisations: IImmobilisation[]) =>
          this.immobilisationService.addImmobilisationToCollectionIfMissing<IImmobilisation>(
            immobilisations,
            this.transfert?.immobilisation
          )
        )
      )
      .subscribe((immobilisations: IImmobilisation[]) => (this.immobilisationsSharedCollection = immobilisations));

    this.agentService
      .query()
      .pipe(map((res: HttpResponse<IAgent[]>) => res.body ?? []))
      .pipe(map((agents: IAgent[]) => this.agentService.addAgentToCollectionIfMissing<IAgent>(agents, this.transfert?.agent)))
      .subscribe((agents: IAgent[]) => (this.agentsSharedCollection = agents));

    this.structureService
      .query()
      .pipe(map((res: HttpResponse<IStructure[]>) => res.body ?? []))
      .pipe(
        map((structures: IStructure[]) =>
          this.structureService.addStructureToCollectionIfMissing<IStructure>(structures, this.transfert?.structure)
        )
      )
      .subscribe((structures: IStructure[]) => (this.structuresSharedCollection = structures));
  }
}
