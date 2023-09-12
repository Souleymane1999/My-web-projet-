import { IAgentStructure, NewAgentStructure } from './agent-structure.model';

export const sampleWithRequiredData: IAgentStructure = {
  id: 65789,
};

export const sampleWithPartialData: IAgentStructure = {
  id: 69659,
};

export const sampleWithFullData: IAgentStructure = {
  id: 36038,
};

export const sampleWithNewData: NewAgentStructure = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
