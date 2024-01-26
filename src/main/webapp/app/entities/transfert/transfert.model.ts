import dayjs from 'dayjs/esm';
import { IImmobilisation } from 'app/entities/immobilisation/immobilisation.model';
import { IAgent } from 'app/entities/agent/agent.model';
import { IStructure } from 'app/entities/structure/structure.model';

export interface ITransfert {
  id: number;
  dateTransfert?: dayjs.Dayjs | null;
  immobilisation?: Pick<IImmobilisation, 'id'> | null;
  agent?: Pick<IAgent, 'id' | 'nomUtilisateur'> | null;
  structure?: Pick<IStructure, 'id' | 'nomStructure'> | null;
}

export type NewTransfert = Omit<ITransfert, 'id'> & { id: null };
