import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITransfert, NewTransfert } from '../transfert.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransfert for edit and NewTransfertFormGroupInput for create.
 */
type TransfertFormGroupInput = ITransfert | PartialWithRequiredKeyOf<NewTransfert>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITransfert | NewTransfert> = Omit<T, 'dateTransfert'> & {
  dateTransfert?: string | null;
};

type TransfertFormRawValue = FormValueOf<ITransfert>;

type NewTransfertFormRawValue = FormValueOf<NewTransfert>;

type TransfertFormDefaults = Pick<NewTransfert, 'id' | 'dateTransfert'>;

type TransfertFormGroupContent = {
  id: FormControl<TransfertFormRawValue['id'] | NewTransfert['id']>;
  dateTransfert: FormControl<TransfertFormRawValue['dateTransfert']>;
  immobilisation: FormControl<TransfertFormRawValue['immobilisation']>;
  struture: FormControl<TransfertFormRawValue['struture']>;
};

export type TransfertFormGroup = FormGroup<TransfertFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TransfertFormService {
  createTransfertFormGroup(transfert: TransfertFormGroupInput = { id: null }): TransfertFormGroup {
    const transfertRawValue = this.convertTransfertToTransfertRawValue({
      ...this.getFormDefaults(),
      ...transfert,
    });
    return new FormGroup<TransfertFormGroupContent>({
      id: new FormControl(
        { value: transfertRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateTransfert: new FormControl(transfertRawValue.dateTransfert),
      immobilisation: new FormControl(transfertRawValue.immobilisation),
      struture: new FormControl(transfertRawValue.struture),
    });
  }

  getTransfert(form: TransfertFormGroup): ITransfert | NewTransfert {
    return this.convertTransfertRawValueToTransfert(form.getRawValue() as TransfertFormRawValue | NewTransfertFormRawValue);
  }

  resetForm(form: TransfertFormGroup, transfert: TransfertFormGroupInput): void {
    const transfertRawValue = this.convertTransfertToTransfertRawValue({ ...this.getFormDefaults(), ...transfert });
    form.reset(
      {
        ...transfertRawValue,
        id: { value: transfertRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TransfertFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateTransfert: currentTime,
    };
  }

  private convertTransfertRawValueToTransfert(rawTransfert: TransfertFormRawValue | NewTransfertFormRawValue): ITransfert | NewTransfert {
    return {
      ...rawTransfert,
      dateTransfert: dayjs(rawTransfert.dateTransfert, DATE_TIME_FORMAT),
    };
  }

  private convertTransfertToTransfertRawValue(
    transfert: ITransfert | (Partial<NewTransfert> & TransfertFormDefaults)
  ): TransfertFormRawValue | PartialWithRequiredKeyOf<NewTransfertFormRawValue> {
    return {
      ...transfert,
      dateTransfert: transfert.dateTransfert ? transfert.dateTransfert.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
