import dayjs from 'dayjs/esm';

import { ITransfert, NewTransfert } from './transfert.model';

export const sampleWithRequiredData: ITransfert = {
  id: 70083,
};

export const sampleWithPartialData: ITransfert = {
  id: 79898,
};

export const sampleWithFullData: ITransfert = {
  id: 76248,
  dateTransfert: dayjs('2023-08-18T06:55'),
};

export const sampleWithNewData: NewTransfert = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
