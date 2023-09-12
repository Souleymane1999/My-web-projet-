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
  structuresSharedCollection: IStructure[] = [];

  editForm: TransfertFormGroup = this.transfertFormService.createTransfertFormGroup();

  constructor(
    protected transfertService: TransfertService,
    protected transfertFormService: TransfertFormService,
    protected immobilisationService: ImmobilisationService,
    protected structureService: StructureService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImmobilisation = (o1: IImmobilisation | null, o2: IImmobilisation | null): boolean =>
    this.immobilisationService.compareImmobilisation(o1, o2);

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
    this.structuresSharedCollection = this.structureService.addStructureToCollectionIfMissing<IStructure>(
      this.structuresSharedCollection,
      transfert.struture
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

    this.structureService
      .query()
      .pipe(map((res: HttpResponse<IStructure[]>) => res.body ?? []))
      .pipe(
        map((structures: IStructure[]) =>
          this.structureService.addStructureToCollectionIfMissing<IStructure>(structures, this.transfert?.struture)
        )
      )
      .subscribe((structures: IStructure[]) => (this.structuresSharedCollection = structures));
  }
}
