import dayjs from 'dayjs/esm';
import { IImmobilisation } from 'app/entities/immobilisation/immobilisation.model';

export interface IMaintenance {
  id: number;
  dateMaintenance?: dayjs.Dayjs | null;
  description?: string | null;
  responsable?: string | null;
  coutMaintenance?: string | null;
  immobilisation?: Pick<IImmobilisation, 'id'> | null;
}

export type NewMaintenance = Omit<IMaintenance, 'id'> & { id: null };
