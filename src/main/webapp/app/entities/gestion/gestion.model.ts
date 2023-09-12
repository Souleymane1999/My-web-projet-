export interface IGestion {
  id: number;
  typeGestion?: string | null;
}

export type NewGestion = Omit<IGestion, 'id'> & { id: null };
