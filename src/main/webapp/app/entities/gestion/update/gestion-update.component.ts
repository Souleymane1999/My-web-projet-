import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { GestionFormService, GestionFormGroup } from './gestion-form.service';
import { IGestion } from '../gestion.model';
import { GestionService } from '../service/gestion.service';

@Component({
  standalone: true,
  selector: 'jhi-gestion-update',
  templateUrl: './gestion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GestionUpdateComponent implements OnInit {
  isSaving = false;
  gestion: IGestion | null = null;

  editForm: GestionFormGroup = this.gestionFormService.createGestionFormGroup();

  constructor(
    protected gestionService: GestionService,
    protected gestionFormService: GestionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gestion }) => {
      this.gestion = gestion;
      if (gestion) {
        this.updateForm(gestion);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gestion = this.gestionFormService.getGestion(this.editForm);
    if (gestion.id !== null) {
      this.subscribeToSaveResponse(this.gestionService.update(gestion));
    } else {
      this.subscribeToSaveResponse(this.gestionService.create(gestion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGestion>>): void {
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

  protected updateForm(gestion: IGestion): void {
    this.gestion = gestion;
    this.gestionFormService.resetForm(this.editForm, gestion);
  }
}
