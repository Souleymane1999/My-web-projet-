import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMaintenance, NewMaintenance } from '../maintenance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaintenance for edit and NewMaintenanceFormGroupInput for create.
 */
type MaintenanceFormGroupInput = IMaintenance | PartialWithRequiredKeyOf<NewMaintenance>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMaintenance | NewMaintenance> = Omit<T, 'dateMaintenance'> & {
  dateMaintenance?: string | null;
};

type MaintenanceFormRawValue = FormValueOf<IMaintenance>;

type NewMaintenanceFormRawValue = FormValueOf<NewMaintenance>;

type MaintenanceFormDefaults = Pick<NewMaintenance, 'id' | 'dateMaintenance'>;

type MaintenanceFormGroupContent = {
  id: FormControl<MaintenanceFormRawValue['id'] | NewMaintenance['id']>;
  dateMaintenance: FormControl<MaintenanceFormRawValue['dateMaintenance']>;
  description: FormControl<MaintenanceFormRawValue['description']>;
  responsable: FormControl<MaintenanceFormRawValue['responsable']>;
  coutMaintenance: FormControl<MaintenanceFormRawValue['coutMaintenance']>;
  immobilisation: FormControl<MaintenanceFormRawValue['immobilisation']>;
};

export type MaintenanceFormGroup = FormGroup<MaintenanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MaintenanceFormService {
  createMaintenanceFormGroup(maintenance: MaintenanceFormGroupInput = { id: null }): MaintenanceFormGroup {
    const maintenanceRawValue = this.convertMaintenanceToMaintenanceRawValue({
      ...this.getFormDefaults(),
      ...maintenance,
    });
    return new FormGroup<MaintenanceFormGroupContent>({
      id: new FormControl(
        { value: maintenanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      dateMaintenance: new FormControl(maintenanceRawValue.dateMaintenance),
      description: new FormControl(maintenanceRawValue.description),
      responsable: new FormControl(maintenanceRawValue.responsable),
      coutMaintenance: new FormControl(maintenanceRawValue.coutMaintenance),
      immobilisation: new FormControl(maintenanceRawValue.immobilisation),
    });
  }

  getMaintenance(form: MaintenanceFormGroup): IMaintenance | NewMaintenance {
    return this.convertMaintenanceRawValueToMaintenance(form.getRawValue() as MaintenanceFormRawValue | NewMaintenanceFormRawValue);
  }

  resetForm(form: MaintenanceFormGroup, maintenance: MaintenanceFormGroupInput): void {
    const maintenanceRawValue = this.convertMaintenanceToMaintenanceRawValue({ ...this.getFormDefaults(), ...maintenance });
    form.reset(
      {
        ...maintenanceRawValue,
        id: { value: maintenanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MaintenanceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateMaintenance: currentTime,
    };
  }

  private convertMaintenanceRawValueToMaintenance(
    rawMaintenance: MaintenanceFormRawValue | NewMaintenanceFormRawValue
  ): IMaintenance | NewMaintenance {
    return {
      ...rawMaintenance,
      dateMaintenance: dayjs(rawMaintenance.dateMaintenance, DATE_TIME_FORMAT),
    };
  }

  private convertMaintenanceToMaintenanceRawValue(
    maintenance: IMaintenance | (Partial<NewMaintenance> & MaintenanceFormDefaults)
  ): MaintenanceFormRawValue | PartialWithRequiredKeyOf<NewMaintenanceFormRawValue> {
    return {
      ...maintenance,
      dateMaintenance: maintenance.dateMaintenance ? maintenance.dateMaintenance.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
