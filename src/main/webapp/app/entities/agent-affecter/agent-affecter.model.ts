export interface IAgentAffecter {
  id: number;
  matricule?: string | null;
  nomUtilisateur?: string | null;
  prenomUtilisateur?: string | null;
  poste?: string | null;
  adresse?: string | null;
  telephone?: string | null;
}

export type NewAgentAffecter = Omit<IAgentAffecter, 'id'> & { id: null };
