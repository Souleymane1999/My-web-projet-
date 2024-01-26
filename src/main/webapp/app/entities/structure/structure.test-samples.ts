import { IStructure, NewStructure } from './structure.model';

export const sampleWithRequiredData: IStructure = {
  id: 58199,
};

export const sampleWithPartialData: IStructure = {
  id: 19700,
};

export const sampleWithFullData: IStructure = {
  id: 92640,
  nomStructure: 'Femme Magnifique',
  adresseStructure: 'distributed b',
};

export const sampleWithNewData: NewStructure = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
