import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICategorie, NewCategorie } from '../categorie.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategorie for edit and NewCategorieFormGroupInput for create.
 */
type CategorieFormGroupInput = ICategorie | PartialWithRequiredKeyOf<NewCategorie>;

type CategorieFormDefaults = Pick<NewCategorie, 'id' | 'nomCategorie'>;

type CategorieFormGroupContent = {
  id: FormControl<ICategorie['id'] | NewCategorie['id']>;
  nomCategorie: FormControl<ICategorie['nomCategorie'] | NewCategorie['nomCategorie']>;
};

export type CategorieFormGroup = FormGroup<CategorieFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CategorieFormService {
  createCategorieFormGroup(categorie: CategorieFormGroupInput = { id: null }): CategorieFormGroup {
    const categorieRawValue = {
      ...this.getFormDefaults(),
      ...categorie,
    };
    return new FormGroup<CategorieFormGroupContent>({
      id: new FormControl(
        { value: categorieRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomCategorie: new FormControl(categorieRawValue.nomCategorie),
    });
  }

  getCategorie(form: CategorieFormGroup): ICategorie | NewCategorie {
    return form.getRawValue() as ICategorie | NewCategorie;
  }

  resetForm(form: CategorieFormGroup, categorie: CategorieFormGroupInput): void {
    const categorieRawValue = { ...this.getFormDefaults(), ...categorie };
    form.reset(
      {
        ...categorieRawValue,
        id: { value: categorieRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CategorieFormDefaults {
    return {
      id: null,
      nomCategorie: null,
    };
  }
}
