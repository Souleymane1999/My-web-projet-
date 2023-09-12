import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILocalite, NewLocalite } from '../localite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocalite for edit and NewLocaliteFormGroupInput for create.
 */
type LocaliteFormGroupInput = ILocalite | PartialWithRequiredKeyOf<NewLocalite>;

type LocaliteFormDefaults = Pick<NewLocalite, 'id'>;

type LocaliteFormGroupContent = {
  id: FormControl<ILocalite['id'] | NewLocalite['id']>;
  nomLocalite: FormControl<ILocalite['nomLocalite']>;
  codePostal: FormControl<ILocalite['codePostal']>;
};

export type LocaliteFormGroup = FormGroup<LocaliteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocaliteFormService {
  createLocaliteFormGroup(localite: LocaliteFormGroupInput = { id: null }): LocaliteFormGroup {
    const localiteRawValue = {
      ...this.getFormDefaults(),
      ...localite,
    };
    return new FormGroup<LocaliteFormGroupContent>({
      id: new FormControl(
        { value: localiteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomLocalite: new FormControl(localiteRawValue.nomLocalite),
      codePostal: new FormControl(localiteRawValue.codePostal),
    });
  }

  getLocalite(form: LocaliteFormGroup): ILocalite | NewLocalite {
    return form.getRawValue() as ILocalite | NewLocalite;
  }

  resetForm(form: LocaliteFormGroup, localite: LocaliteFormGroupInput): void {
    const localiteRawValue = { ...this.getFormDefaults(), ...localite };
    form.reset(
      {
        ...localiteRawValue,
        id: { value: localiteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LocaliteFormDefaults {
    return {
      id: null,
    };
  }
}
