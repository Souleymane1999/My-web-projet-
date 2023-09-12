import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ImmobilisationFormService, ImmobilisationFormGroup } from './immobilisation-form.service';
import { IImmobilisation } from '../immobilisation.model';
import { ImmobilisationService } from '../service/immobilisation.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { IGestion } from 'app/entities/gestion/gestion.model';
import { GestionService } from 'app/entities/gestion/service/gestion.service';

@Component({
  standalone: true,
  selector: 'jhi-immobilisation-update',
  templateUrl: './immobilisation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImmobilisationUpdateComponent implements OnInit {
  isSaving = false;
  immobilisation: IImmobilisation | null = null;

  categoriesSharedCollection: ICategorie[] = [];
  gestionsSharedCollection: IGestion[] = [];

  editForm: ImmobilisationFormGroup = this.immobilisationFormService.createImmobilisationFormGroup();

  constructor(
    protected immobilisationService: ImmobilisationService,
    protected immobilisationFormService: ImmobilisationFormService,
    protected categorieService: CategorieService,
    protected gestionService: GestionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCategorie = (o1: ICategorie | null, o2: ICategorie | null): boolean => this.categorieService.compareCategorie(o1, o2);

  compareGestion = (o1: IGestion | null, o2: IGestion | null): boolean => this.gestionService.compareGestion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ immobilisation }) => {
      this.immobilisation = immobilisation;
      if (immobilisation) {
        this.updateForm(immobilisation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const immobilisation = this.immobilisationFormService.getImmobilisation(this.editForm);
    if (immobilisation.id !== null) {
      this.subscribeToSaveResponse(this.immobilisationService.update(immobilisation));
    } else {
      this.subscribeToSaveResponse(this.immobilisationService.create(immobilisation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImmobilisation>>): void {
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

  protected updateForm(immobilisation: IImmobilisation): void {
    this.immobilisation = immobilisation;
    this.immobilisationFormService.resetForm(this.editForm, immobilisation);

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(
      this.categoriesSharedCollection,
      immobilisation.categorie
    );
    this.gestionsSharedCollection = this.gestionService.addGestionToCollectionIfMissing<IGestion>(
      this.gestionsSharedCollection,
      immobilisation.gestion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing<ICategorie>(categories, this.immobilisation?.categorie)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));

    this.gestionService
      .query()
      .pipe(map((res: HttpResponse<IGestion[]>) => res.body ?? []))
      .pipe(
        map((gestions: IGestion[]) => this.gestionService.addGestionToCollectionIfMissing<IGestion>(gestions, this.immobilisation?.gestion))
      )
      .subscribe((gestions: IGestion[]) => (this.gestionsSharedCollection = gestions));
  }
}
