import { ICategorie, NewCategorie } from './categorie.model';

export const sampleWithRequiredData: ICategorie = {
  id: 21970,
};

export const sampleWithPartialData: ICategorie = {
  id: 80633,
};

export const sampleWithFullData: ICategorie = {
  id: 35745,
  nomCategorie: 'a Recyclé',
};

export const sampleWithNewData: NewCategorie = {
  id: null,
  nomCategorie: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
