import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocalite, NewLocalite } from '../localite.model';

export type PartialUpdateLocalite = Partial<ILocalite> & Pick<ILocalite, 'id'>;

export type EntityResponseType = HttpResponse<ILocalite>;
export type EntityArrayResponseType = HttpResponse<ILocalite[]>;

@Injectable({ providedIn: 'root' })
export class LocaliteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/localites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(localite: NewLocalite): Observable<EntityResponseType> {
    return this.http.post<ILocalite>(this.resourceUrl, localite, { observe: 'response' });
  }

  update(localite: ILocalite): Observable<EntityResponseType> {
    return this.http.put<ILocalite>(`${this.resourceUrl}/${this.getLocaliteIdentifier(localite)}`, localite, { observe: 'response' });
  }

  partialUpdate(localite: PartialUpdateLocalite): Observable<EntityResponseType> {
    return this.http.patch<ILocalite>(`${this.resourceUrl}/${this.getLocaliteIdentifier(localite)}`, localite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocalite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLocaliteIdentifier(localite: Pick<ILocalite, 'id'>): number {
    return localite.id;
  }

  compareLocalite(o1: Pick<ILocalite, 'id'> | null, o2: Pick<ILocalite, 'id'> | null): boolean {
    return o1 && o2 ? this.getLocaliteIdentifier(o1) === this.getLocaliteIdentifier(o2) : o1 === o2;
  }

  addLocaliteToCollectionIfMissing<Type extends Pick<ILocalite, 'id'>>(
    localiteCollection: Type[],
    ...localitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const localites: Type[] = localitesToCheck.filter(isPresent);
    if (localites.length > 0) {
      const localiteCollectionIdentifiers = localiteCollection.map(localiteItem => this.getLocaliteIdentifier(localiteItem)!);
      const localitesToAdd = localites.filter(localiteItem => {
        const localiteIdentifier = this.getLocaliteIdentifier(localiteItem);
        if (localiteCollectionIdentifiers.includes(localiteIdentifier)) {
          return false;
        }
        localiteCollectionIdentifiers.push(localiteIdentifier);
        return true;
      });
      return [...localitesToAdd, ...localiteCollection];
    }
    return localiteCollection;
  }
}
