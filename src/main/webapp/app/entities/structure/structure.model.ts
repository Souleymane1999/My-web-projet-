import { ILocalite } from 'app/entities/localite/localite.model';

export interface IStructure {
  id: number;
  nomStructure?: string | null;
  adresseStructure?: string | null;
  localite?: Pick<ILocalite, 'id'> | null;
}

export type NewStructure = Omit<IStructure, 'id'> & { id: null };
