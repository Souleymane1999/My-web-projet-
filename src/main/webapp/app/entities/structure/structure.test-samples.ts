import { IStructure, NewStructure } from './structure.model';

export const sampleWithRequiredData: IStructure = {
  id: 89094,
};

export const sampleWithPartialData: IStructure = {
  id: 82556,
  nomStructure: 'Cerium Thaïlande',
  adresseStructure: 'Jaguar a',
};

export const sampleWithFullData: IStructure = {
  id: 40456,
  nomStructure: '24/7',
  adresseStructure: 'modular mobile Génial',
};

export const sampleWithNewData: NewStructure = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
