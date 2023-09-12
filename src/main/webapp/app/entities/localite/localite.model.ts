export interface ILocalite {
  id: number;
  nomLocalite?: string | null;
  codePostal?: string | null;
}

export type NewLocalite = Omit<ILocalite, 'id'> & { id: null };
