import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGestion, NewGestion } from '../gestion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGestion for edit and NewGestionFormGroupInput for create.
 */
type GestionFormGroupInput = IGestion | PartialWithRequiredKeyOf<NewGestion>;

type GestionFormDefaults = Pick<NewGestion, 'id'>;

type GestionFormGroupContent = {
  id: FormControl<IGestion['id'] | NewGestion['id']>;
  typeGestion: FormControl<IGestion['typeGestion']>;
};

export type GestionFormGroup = FormGroup<GestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GestionFormService {
  createGestionFormGroup(gestion: GestionFormGroupInput = { id: null }): GestionFormGroup {
    const gestionRawValue = {
      ...this.getFormDefaults(),
      ...gestion,
    };
    return new FormGroup<GestionFormGroupContent>({
      id: new FormControl(
        { value: gestionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      typeGestion: new FormControl(gestionRawValue.typeGestion),
    });
  }

  getGestion(form: GestionFormGroup): IGestion | NewGestion {
    return form.getRawValue() as IGestion | NewGestion;
  }

  resetForm(form: GestionFormGroup, gestion: GestionFormGroupInput): void {
    const gestionRawValue = { ...this.getFormDefaults(), ...gestion };
    form.reset(
      {
        ...gestionRawValue,
        id: { value: gestionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GestionFormDefaults {
    return {
      id: null,
    };
  }
}
