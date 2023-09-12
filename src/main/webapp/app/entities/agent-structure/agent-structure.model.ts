import { IStructure } from 'app/entities/structure/structure.model';
import { IAgentAffecter } from 'app/entities/agent-affecter/agent-affecter.model';

export interface IAgentStructure {
  id: number;
  struture?: Pick<IStructure, 'id'> | null;
  agent?: Pick<IAgentAffecter, 'id'> | null;
}

export type NewAgentStructure = Omit<IAgentStructure, 'id'> & { id: null };
