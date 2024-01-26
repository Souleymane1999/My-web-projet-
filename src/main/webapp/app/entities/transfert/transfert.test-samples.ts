import dayjs from 'dayjs/esm';

import { ITransfert, NewTransfert } from './transfert.model';

export const sampleWithRequiredData: ITransfert = {
  id: 82765,
};

export const sampleWithPartialData: ITransfert = {
  id: 69764,
};

export const sampleWithFullData: ITransfert = {
  id: 61484,
  dateTransfert: dayjs('2023-08-17T21:53'),
};

export const sampleWithNewData: NewTransfert = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
