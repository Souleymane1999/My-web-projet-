import { IGestion, NewGestion } from './gestion.model';

export const sampleWithRequiredData: IGestion = {
  id: 93732,
};

export const sampleWithPartialData: IGestion = {
  id: 78008,
};

export const sampleWithFullData: IGestion = {
  id: 93415,
  typeGestion: 'Nord',
};

export const sampleWithNewData: NewGestion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
