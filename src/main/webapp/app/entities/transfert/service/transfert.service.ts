import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransfert, NewTransfert } from '../transfert.model';

export type PartialUpdateTransfert = Partial<ITransfert> & Pick<ITransfert, 'id'>;

type RestOf<T extends ITransfert | NewTransfert> = Omit<T, 'dateTransfert'> & {
  dateTransfert?: string | null;
};

export type RestTransfert = RestOf<ITransfert>;

export type NewRestTransfert = RestOf<NewTransfert>;

export type PartialUpdateRestTransfert = RestOf<PartialUpdateTransfert>;

export type EntityResponseType = HttpResponse<ITransfert>;
export type EntityArrayResponseType = HttpResponse<ITransfert[]>;

@Injectable({ providedIn: 'root' })
export class TransfertService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transferts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transfert: NewTransfert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transfert);
    return this.http
      .post<RestTransfert>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transfert: ITransfert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transfert);
    return this.http
      .put<RestTransfert>(`${this.resourceUrl}/${this.getTransfertIdentifier(transfert)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transfert: PartialUpdateTransfert): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transfert);
    return this.http
      .patch<RestTransfert>(`${this.resourceUrl}/${this.getTransfertIdentifier(transfert)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTransfert>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransfert[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTransfertIdentifier(transfert: Pick<ITransfert, 'id'>): number {
    return transfert.id;
  }

  compareTransfert(o1: Pick<ITransfert, 'id'> | null, o2: Pick<ITransfert, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransfertIdentifier(o1) === this.getTransfertIdentifier(o2) : o1 === o2;
  }

  addTransfertToCollectionIfMissing<Type extends Pick<ITransfert, 'id'>>(
    transfertCollection: Type[],
    ...transfertsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transferts: Type[] = transfertsToCheck.filter(isPresent);
    if (transferts.length > 0) {
      const transfertCollectionIdentifiers = transfertCollection.map(transfertItem => this.getTransfertIdentifier(transfertItem)!);
      const transfertsToAdd = transferts.filter(transfertItem => {
        const transfertIdentifier = this.getTransfertIdentifier(transfertItem);
        if (transfertCollectionIdentifiers.includes(transfertIdentifier)) {
          return false;
        }
        transfertCollectionIdentifiers.push(transfertIdentifier);
        return true;
      });
      return [...transfertsToAdd, ...transfertCollection];
    }
    return transfertCollection;
  }

  protected convertDateFromClient<T extends ITransfert | NewTransfert | PartialUpdateTransfert>(transfert: T): RestOf<T> {
    return {
      ...transfert,
      dateTransfert: transfert.dateTransfert?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTransfert: RestTransfert): ITransfert {
    return {
      ...restTransfert,
      dateTransfert: restTransfert.dateTransfert ? dayjs(restTransfert.dateTransfert) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTransfert>): HttpResponse<ITransfert> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTransfert[]>): HttpResponse<ITransfert[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
