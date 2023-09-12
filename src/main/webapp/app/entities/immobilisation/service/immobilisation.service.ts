import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImmobilisation, NewImmobilisation } from '../immobilisation.model';

export type PartialUpdateImmobilisation = Partial<IImmobilisation> & Pick<IImmobilisation, 'id'>;

type RestOf<T extends IImmobilisation | NewImmobilisation> = Omit<T, 'dateAcquisition'> & {
  dateAcquisition?: string | null;
};

export type RestImmobilisation = RestOf<IImmobilisation>;

export type NewRestImmobilisation = RestOf<NewImmobilisation>;

export type PartialUpdateRestImmobilisation = RestOf<PartialUpdateImmobilisation>;

export type EntityResponseType = HttpResponse<IImmobilisation>;
export type EntityArrayResponseType = HttpResponse<IImmobilisation[]>;

@Injectable({ providedIn: 'root' })
export class ImmobilisationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/immobilisations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(immobilisation: NewImmobilisation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(immobilisation);
    return this.http
      .post<RestImmobilisation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(immobilisation: IImmobilisation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(immobilisation);
    return this.http
      .put<RestImmobilisation>(`${this.resourceUrl}/${this.getImmobilisationIdentifier(immobilisation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(immobilisation: PartialUpdateImmobilisation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(immobilisation);
    return this.http
      .patch<RestImmobilisation>(`${this.resourceUrl}/${this.getImmobilisationIdentifier(immobilisation)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestImmobilisation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestImmobilisation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getImmobilisationIdentifier(immobilisation: Pick<IImmobilisation, 'id'>): number {
    return immobilisation.id;
  }

  compareImmobilisation(o1: Pick<IImmobilisation, 'id'> | null, o2: Pick<IImmobilisation, 'id'> | null): boolean {
    return o1 && o2 ? this.getImmobilisationIdentifier(o1) === this.getImmobilisationIdentifier(o2) : o1 === o2;
  }

  addImmobilisationToCollectionIfMissing<Type extends Pick<IImmobilisation, 'id'>>(
    immobilisationCollection: Type[],
    ...immobilisationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const immobilisations: Type[] = immobilisationsToCheck.filter(isPresent);
    if (immobilisations.length > 0) {
      const immobilisationCollectionIdentifiers = immobilisationCollection.map(
        immobilisationItem => this.getImmobilisationIdentifier(immobilisationItem)!
      );
      const immobilisationsToAdd = immobilisations.filter(immobilisationItem => {
        const immobilisationIdentifier = this.getImmobilisationIdentifier(immobilisationItem);
        if (immobilisationCollectionIdentifiers.includes(immobilisationIdentifier)) {
          return false;
        }
        immobilisationCollectionIdentifiers.push(immobilisationIdentifier);
        return true;
      });
      return [...immobilisationsToAdd, ...immobilisationCollection];
    }
    return immobilisationCollection;
  }

  protected convertDateFromClient<T extends IImmobilisation | NewImmobilisation | PartialUpdateImmobilisation>(
    immobilisation: T
  ): RestOf<T> {
    return {
      ...immobilisation,
      dateAcquisition: immobilisation.dateAcquisition?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restImmobilisation: RestImmobilisation): IImmobilisation {
    return {
      ...restImmobilisation,
      dateAcquisition: restImmobilisation.dateAcquisition ? dayjs(restImmobilisation.dateAcquisition) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestImmobilisation>): HttpResponse<IImmobilisation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestImmobilisation[]>): HttpResponse<IImmobilisation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
