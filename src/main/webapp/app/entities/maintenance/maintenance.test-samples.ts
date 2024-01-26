import dayjs from 'dayjs/esm';

import { IMaintenance, NewMaintenance } from './maintenance.model';

export const sampleWithRequiredData: IMaintenance = {
  id: 56671,
};

export const sampleWithPartialData: IMaintenance = {
  id: 14180,
  description: 'polonais Diesel',
};

export const sampleWithFullData: IMaintenance = {
  id: 74611,
  dateMaintenance: dayjs('2023-08-18T10:02'),
  description: 'b interactive content',
  responsable: 'marcher Analyste haptic',
  coutMaintenance: 'scale Ergonomique',
};

export const sampleWithNewData: NewMaintenance = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
