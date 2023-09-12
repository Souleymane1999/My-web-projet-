import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LocaliteFormService, LocaliteFormGroup } from './localite-form.service';
import { ILocalite } from '../localite.model';
import { LocaliteService } from '../service/localite.service';

@Component({
  standalone: true,
  selector: 'jhi-localite-update',
  templateUrl: './localite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocaliteUpdateComponent implements OnInit {
  isSaving = false;
  localite: ILocalite | null = null;

  editForm: LocaliteFormGroup = this.localiteFormService.createLocaliteFormGroup();

  constructor(
    protected localiteService: LocaliteService,
    protected localiteFormService: LocaliteFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ localite }) => {
      this.localite = localite;
      if (localite) {
        this.updateForm(localite);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const localite = this.localiteFormService.getLocalite(this.editForm);
    if (localite.id !== null) {
      this.subscribeToSaveResponse(this.localiteService.update(localite));
    } else {
      this.subscribeToSaveResponse(this.localiteService.create(localite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocalite>>): void {
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

  protected updateForm(localite: ILocalite): void {
    this.localite = localite;
    this.localiteFormService.resetForm(this.editForm, localite);
  }
}
