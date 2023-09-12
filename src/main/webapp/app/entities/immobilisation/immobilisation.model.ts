import dayjs from 'dayjs/esm';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { IGestion } from 'app/entities/gestion/gestion.model';

export interface IImmobilisation {
  id: number;
  nom?: string | null;
  description?: string | null;
  valeur?: string | null;
  etat?: string | null;
  quantite?: string | null;
  dateAcquisition?: dayjs.Dayjs | null;
  typeAmortissement?: string | null;
  dureeAmortissement?: string | null;
  categorie?: Pick<ICategorie, 'id' | 'nomCategorie'> | null;
  gestion?: Pick<IGestion, 'id'> | null;
}

export type NewImmobilisation = Omit<IImmobilisation, 'id'> & { id: null };
