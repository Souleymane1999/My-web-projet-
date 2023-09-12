import dayjs from 'dayjs/esm';

import { IMaintenance, NewMaintenance } from './maintenance.model';

export const sampleWithRequiredData: IMaintenance = {
  id: 23507,
};

export const sampleWithPartialData: IMaintenance = {
  id: 41619,
  dateMaintenance: dayjs('2023-08-17T11:19'),
  description: 'c b cargo',
  responsable: 'Cambridgeshire orange b',
};

export const sampleWithFullData: IMaintenance = {
  id: 25872,
  dateMaintenance: dayjs('2023-08-17T16:57'),
  description: 'back',
  responsable: 'mindshare haptic a',
  coutMaintenance: 'fourbe Nord',
};

export const sampleWithNewData: NewMaintenance = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
