import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IImmobilisation, NewImmobilisation } from '../immobilisation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImmobilisation for edit and NewImmobilisationFormGroupInput for create.
 */
type ImmobilisationFormGroupInput = IImmobilisation | PartialWithRequiredKeyOf<NewImmobilisation>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IImmobilisation | NewImmobilisation> = Omit<T, 'dateAcquisition'> & {
  dateAcquisition?: string | null;
};

type ImmobilisationFormRawValue = FormValueOf<IImmobilisation>;

type NewImmobilisationFormRawValue = FormValueOf<NewImmobilisation>;

type ImmobilisationFormDefaults = Pick<NewImmobilisation, 'id' | 'dateAcquisition'>;

type ImmobilisationFormGroupContent = {
  id: FormControl<ImmobilisationFormRawValue['id'] | NewImmobilisation['id']>;
  nom: FormControl<ImmobilisationFormRawValue['nom']>;
  description: FormControl<ImmobilisationFormRawValue['description']>;
  valeur: FormControl<ImmobilisationFormRawValue['valeur']>;
  etat: FormControl<ImmobilisationFormRawValue['etat']>;
  quantite: FormControl<ImmobilisationFormRawValue['quantite']>;
  dateAcquisition: FormControl<ImmobilisationFormRawValue['dateAcquisition']>;
  typeAmortissement: FormControl<ImmobilisationFormRawValue['typeAmortissement']>;
  dureeAmortissement: FormControl<ImmobilisationFormRawValue['dureeAmortissement']>;
  categorie: FormControl<ImmobilisationFormRawValue['categorie']>;
  gestion: FormControl<ImmobilisationFormRawValue['gestion']>;
};

export type ImmobilisationFormGroup = FormGroup<ImmobilisationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImmobilisationFormService {
  createImmobilisationFormGroup(immobilisation: ImmobilisationFormGroupInput = { id: null }): ImmobilisationFormGroup {
    const immobilisationRawValue = this.convertImmobilisationToImmobilisationRawValue({
      ...this.getFormDefaults(),
      ...immobilisation,
    });
    return new FormGroup<ImmobilisationFormGroupContent>({
      id: new FormControl(
        { value: immobilisationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nom: new FormControl(immobilisationRawValue.nom),
      description: new FormControl(immobilisationRawValue.description),
      valeur: new FormControl(immobilisationRawValue.valeur),
      etat: new FormControl(immobilisationRawValue.etat),
      quantite: new FormControl(immobilisationRawValue.quantite),
      dateAcquisition: new FormControl(immobilisationRawValue.dateAcquisition),
      typeAmortissement: new FormControl(immobilisationRawValue.typeAmortissement),
      dureeAmortissement: new FormControl(immobilisationRawValue.dureeAmortissement),
      categorie: new FormControl(immobilisationRawValue.categorie),
      gestion: new FormControl(immobilisationRawValue.gestion),
    });
  }

  getImmobilisation(form: ImmobilisationFormGroup): IImmobilisation | NewImmobilisation {
    return this.convertImmobilisationRawValueToImmobilisation(
      form.getRawValue() as ImmobilisationFormRawValue | NewImmobilisationFormRawValue
    );
  }

  resetForm(form: ImmobilisationFormGroup, immobilisation: ImmobilisationFormGroupInput): void {
    const immobilisationRawValue = this.convertImmobilisationToImmobilisationRawValue({ ...this.getFormDefaults(), ...immobilisation });
    form.reset(
      {
        ...immobilisationRawValue,
        id: { value: immobilisationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ImmobilisationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateAcquisition: currentTime,
    };
  }

  private convertImmobilisationRawValueToImmobilisation(
    rawImmobilisation: ImmobilisationFormRawValue | NewImmobilisationFormRawValue
  ): IImmobilisation | NewImmobilisation {
    return {
      ...rawImmobilisation,
      dateAcquisition: dayjs(rawImmobilisation.dateAcquisition, DATE_TIME_FORMAT),
    };
  }

  private convertImmobilisationToImmobilisationRawValue(
    immobilisation: IImmobilisation | (Partial<NewImmobilisation> & ImmobilisationFormDefaults)
  ): ImmobilisationFormRawValue | PartialWithRequiredKeyOf<NewImmobilisationFormRawValue> {
    return {
      ...immobilisation,
      dateAcquisition: immobilisation.dateAcquisition ? immobilisation.dateAcquisition.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
