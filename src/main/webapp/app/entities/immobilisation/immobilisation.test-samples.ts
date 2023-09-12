import dayjs from 'dayjs/esm';

import { IImmobilisation, NewImmobilisation } from './immobilisation.model';

export const sampleWithRequiredData: IImmobilisation = {
  id: 91992,
};

export const sampleWithPartialData: IImmobilisation = {
  id: 21657,
  description: 'que',
  valeur: 'outre malachite Lofi',
  etat: 'nulla',
  quantite: 'Pangender',
  dateAcquisition: dayjs('2023-08-18T05:22'),
  typeAmortissement: 'Chaise generation b',
};

export const sampleWithFullData: IImmobilisation = {
  id: 7288,
  nom: 'Corse Borders Variété',
  description: 'suscipit Agent Haïti',
  valeur: 'nihil',
  etat: 'Bugatti anthracite expedita',
  quantite: 'firewall',
  dateAcquisition: dayjs('2023-08-18T07:23'),
  typeAmortissement: 'hôte hour',
  dureeAmortissement: 'Berkshire Vénissieux syrienne',
};

export const sampleWithNewData: NewImmobilisation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
