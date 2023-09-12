import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { StructureFormService, StructureFormGroup } from './structure-form.service';
import { IStructure } from '../structure.model';
import { StructureService } from '../service/structure.service';
import { ILocalite } from 'app/entities/localite/localite.model';
import { LocaliteService } from 'app/entities/localite/service/localite.service';

@Component({
  standalone: true,
  selector: 'jhi-structure-update',
  templateUrl: './structure-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StructureUpdateComponent implements OnInit {
  isSaving = false;
  structure: IStructure | null = null;

  localitesSharedCollection: ILocalite[] = [];

  editForm: StructureFormGroup = this.structureFormService.createStructureFormGroup();

  constructor(
    protected structureService: StructureService,
    protected structureFormService: StructureFormService,
    protected localiteService: LocaliteService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareLocalite = (o1: ILocalite | null, o2: ILocalite | null): boolean => this.localiteService.compareLocalite(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ structure }) => {
      this.structure = structure;
      if (structure) {
        this.updateForm(structure);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const structure = this.structureFormService.getStructure(this.editForm);
    if (structure.id !== null) {
      this.subscribeToSaveResponse(this.structureService.update(structure));
    } else {
      this.subscribeToSaveResponse(this.structureService.create(structure));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStructure>>): void {
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

  protected updateForm(structure: IStructure): void {
    this.structure = structure;
    this.structureFormService.resetForm(this.editForm, structure);

    this.localitesSharedCollection = this.localiteService.addLocaliteToCollectionIfMissing<ILocalite>(
      this.localitesSharedCollection,
      structure.localite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.localiteService
      .query()
      .pipe(map((res: HttpResponse<ILocalite[]>) => res.body ?? []))
      .pipe(
        map((localites: ILocalite[]) =>
          this.localiteService.addLocaliteToCollectionIfMissing<ILocalite>(localites, this.structure?.localite)
        )
      )
      .subscribe((localites: ILocalite[]) => (this.localitesSharedCollection = localites));
  }
}
