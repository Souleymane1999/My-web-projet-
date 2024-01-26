export interface IAgent {
  id: number;
  matricule?: string | null;
  nomUtilisateur?: string | null;
  prenomUtilisateur?: string | null;
  poste?: string | null;
  adresse?: string | null;
  telephone?: string | null;
}

export type NewAgent = Omit<IAgent, 'id'> & { id: null };
