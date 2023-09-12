import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { MaintenanceFormService, MaintenanceFormGroup } from './maintenance-form.service';
import { IMaintenance } from '../maintenance.model';
import { MaintenanceService } from '../service/maintenance.service';
import { IImmobilisation } from 'app/entities/immobilisation/immobilisation.model';
import { ImmobilisationService } from 'app/entities/immobilisation/service/immobilisation.service';

@Component({
  standalone: true,
  selector: 'jhi-maintenance-update',
  templateUrl: './maintenance-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MaintenanceUpdateComponent implements OnInit {
  isSaving = false;
  maintenance: IMaintenance | null = null;

  immobilisationsSharedCollection: IImmobilisation[] = [];

  editForm: MaintenanceFormGroup = this.maintenanceFormService.createMaintenanceFormGroup();

  constructor(
    protected maintenanceService: MaintenanceService,
    protected maintenanceFormService: MaintenanceFormService,
    protected immobilisationService: ImmobilisationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareImmobilisation = (o1: IImmobilisation | null, o2: IImmobilisation | null): boolean =>
    this.immobilisationService.compareImmobilisation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintenance }) => {
      this.maintenance = maintenance;
      if (maintenance) {
        this.updateForm(maintenance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const maintenance = this.maintenanceFormService.getMaintenance(this.editForm);
    if (maintenance.id !== null) {
      this.subscribeToSaveResponse(this.maintenanceService.update(maintenance));
    } else {
      this.subscribeToSaveResponse(this.maintenanceService.create(maintenance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaintenance>>): void {
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

  protected updateForm(maintenance: IMaintenance): void {
    this.maintenance = maintenance;
    this.maintenanceFormService.resetForm(this.editForm, maintenance);

    this.immobilisationsSharedCollection = this.immobilisationService.addImmobilisationToCollectionIfMissing<IImmobilisation>(
      this.immobilisationsSharedCollection,
      maintenance.immobilisation
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
            this.maintenance?.immobilisation
          )
        )
      )
      .subscribe((immobilisations: IImmobilisation[]) => (this.immobilisationsSharedCollection = immobilisations));
  }
}
