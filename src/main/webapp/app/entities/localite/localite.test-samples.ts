import { ILocalite, NewLocalite } from './localite.model';

export const sampleWithRequiredData: ILocalite = {
  id: 54138,
};

export const sampleWithPartialData: ILocalite = {
  id: 12436,
  nomLocalite: 'Doux',
  codePostal: 'protocol du',
};

export const sampleWithFullData: ILocalite = {
  id: 813,
  nomLocalite: 'provident coulomb fort',
  codePostal: 'paiement c',
};

export const sampleWithNewData: NewLocalite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
