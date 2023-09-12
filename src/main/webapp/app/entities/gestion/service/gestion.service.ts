import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGestion, NewGestion } from '../gestion.model';

export type PartialUpdateGestion = Partial<IGestion> & Pick<IGestion, 'id'>;

export type EntityResponseType = HttpResponse<IGestion>;
export type EntityArrayResponseType = HttpResponse<IGestion[]>;

@Injectable({ providedIn: 'root' })
export class GestionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gestions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gestion: NewGestion): Observable<EntityResponseType> {
    return this.http.post<IGestion>(this.resourceUrl, gestion, { observe: 'response' });
  }

  update(gestion: IGestion): Observable<EntityResponseType> {
    return this.http.put<IGestion>(`${this.resourceUrl}/${this.getGestionIdentifier(gestion)}`, gestion, { observe: 'response' });
  }

  partialUpdate(gestion: PartialUpdateGestion): Observable<EntityResponseType> {
    return this.http.patch<IGestion>(`${this.resourceUrl}/${this.getGestionIdentifier(gestion)}`, gestion, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGestion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGestion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGestionIdentifier(gestion: Pick<IGestion, 'id'>): number {
    return gestion.id;
  }

  compareGestion(o1: Pick<IGestion, 'id'> | null, o2: Pick<IGestion, 'id'> | null): boolean {
    return o1 && o2 ? this.getGestionIdentifier(o1) === this.getGestionIdentifier(o2) : o1 === o2;
  }

  addGestionToCollectionIfMissing<Type extends Pick<IGestion, 'id'>>(
    gestionCollection: Type[],
    ...gestionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gestions: Type[] = gestionsToCheck.filter(isPresent);
    if (gestions.length > 0) {
      const gestionCollectionIdentifiers = gestionCollection.map(gestionItem => this.getGestionIdentifier(gestionItem)!);
      const gestionsToAdd = gestions.filter(gestionItem => {
        const gestionIdentifier = this.getGestionIdentifier(gestionItem);
        if (gestionCollectionIdentifiers.includes(gestionIdentifier)) {
          return false;
        }
        gestionCollectionIdentifiers.push(gestionIdentifier);
        return true;
      });
      return [...gestionsToAdd, ...gestionCollection];
    }
    return gestionCollection;
  }
}
