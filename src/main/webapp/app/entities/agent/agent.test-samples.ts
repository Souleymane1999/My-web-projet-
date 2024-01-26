import { IAgent, NewAgent } from './agent.model';

export const sampleWithRequiredData: IAgent = {
  id: 91302,
};

export const sampleWithPartialData: IAgent = {
  id: 66914,
  matricule: 'override que',
  nomUtilisateur: 'Béton',
  adresse: 'protocol Place dolores',
};

export const sampleWithFullData: IAgent = {
  id: 77790,
  matricule: 'Specialiste',
  nomUtilisateur: 'demi BMW Bretagne',
  prenomUtilisateur: 'Streamlined mairie dépôt',
  poste: 'Jardin',
  adresse: 'Avon',
  telephone: '+33 439877812',
};

export const sampleWithNewData: NewAgent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
