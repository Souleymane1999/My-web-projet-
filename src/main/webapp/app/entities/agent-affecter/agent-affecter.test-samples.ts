import { IAgentAffecter, NewAgentAffecter } from './agent-affecter.model';

export const sampleWithRequiredData: IAgentAffecter = {
  id: 20712,
};

export const sampleWithPartialData: IAgentAffecter = {
  id: 51942,
  nomUtilisateur: 'strategy Ouest sincère',
  prenomUtilisateur: 'grandement',
  adresse: 'Quai',
};

export const sampleWithFullData: IAgentAffecter = {
  id: 65814,
  matricule: 'Chlorine comme',
  nomUtilisateur: 'brésilien mobile Nord',
  prenomUtilisateur: 'Agent',
  poste: 'hack',
  adresse: 'Tall',
  telephone: '+33 625298361',
};

export const sampleWithNewData: NewAgentAffecter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
