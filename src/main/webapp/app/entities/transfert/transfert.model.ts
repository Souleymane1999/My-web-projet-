import dayjs from 'dayjs/esm';
import { IImmobilisation } from 'app/entities/immobilisation/immobilisation.model';
import { IStructure } from 'app/entities/structure/structure.model';

export interface ITransfert {
  id: number;
  dateTransfert?: dayjs.Dayjs | null;
  immobilisation?: Pick<IImmobilisation, 'id'> | null;
  struture?: Pick<IStructure, 'id'> | null;
}

export type NewTransfert = Omit<ITransfert, 'id'> & { id: null };
