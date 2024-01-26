import { ILocalite, NewLocalite } from './localite.model';

export const sampleWithRequiredData: ILocalite = {
  id: 98534,
};

export const sampleWithPartialData: ILocalite = {
  id: 68637,
};

export const sampleWithFullData: ILocalite = {
  id: 80411,
  nomLocalite: 'Thaïlande',
  codePostal: 'c enfant Clavier',
};

export const sampleWithNewData: NewLocalite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
