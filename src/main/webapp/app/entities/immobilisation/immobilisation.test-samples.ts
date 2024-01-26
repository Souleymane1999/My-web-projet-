import dayjs from 'dayjs/esm';

import { IImmobilisation, NewImmobilisation } from './immobilisation.model';

export const sampleWithRequiredData: IImmobilisation = {
  id: 21931,
};

export const sampleWithPartialData: IImmobilisation = {
  id: 94312,
  valeur: 'Salade Est',
  etat: 'Lepic Leu',
  quantite: 'Lowrider',
  dateAcquisition: dayjs('2023-08-18T09:17'),
  typeAmortissement: 'Femme',
  dureeAmortissement: 'Femme Benz',
};

export const sampleWithFullData: IImmobilisation = {
  id: 43837,
  nom: 'Chaussures',
  description: 'Incroyable paille Haïti',
  valeur: 'solitaire que',
  etat: 'capacitor middleware',
  quantite: 'de trop',
  dateAcquisition: dayjs('2023-08-18T01:34'),
  typeAmortissement: 'compressing plic',
  dureeAmortissement: 'mouiller consectetur Reggae',
};

export const sampleWithNewData: NewImmobilisation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
