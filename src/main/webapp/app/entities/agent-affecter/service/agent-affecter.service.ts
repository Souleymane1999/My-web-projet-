import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgentAffecter, NewAgentAffecter } from '../agent-affecter.model';

export type PartialUpdateAgentAffecter = Partial<IAgentAffecter> & Pick<IAgentAffecter, 'id'>;

export type EntityResponseType = HttpResponse<IAgentAffecter>;
export type EntityArrayResponseType = HttpResponse<IAgentAffecter[]>;

@Injectable({ providedIn: 'root' })
export class AgentAffecterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agent-affecters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agentAffecter: NewAgentAffecter): Observable<EntityResponseType> {
    return this.http.post<IAgentAffecter>(this.resourceUrl, agentAffecter, { observe: 'response' });
  }

  update(agentAffecter: IAgentAffecter): Observable<EntityResponseType> {
    return this.http.put<IAgentAffecter>(`${this.resourceUrl}/${this.getAgentAffecterIdentifier(agentAffecter)}`, agentAffecter, {
      observe: 'response',
    });
  }

  partialUpdate(agentAffecter: PartialUpdateAgentAffecter): Observable<EntityResponseType> {
    return this.http.patch<IAgentAffecter>(`${this.resourceUrl}/${this.getAgentAffecterIdentifier(agentAffecter)}`, agentAffecter, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgentAffecter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgentAffecter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgentAffecterIdentifier(agentAffecter: Pick<IAgentAffecter, 'id'>): number {
    return agentAffecter.id;
  }

  compareAgentAffecter(o1: Pick<IAgentAffecter, 'id'> | null, o2: Pick<IAgentAffecter, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgentAffecterIdentifier(o1) === this.getAgentAffecterIdentifier(o2) : o1 === o2;
  }

  addAgentAffecterToCollectionIfMissing<Type extends Pick<IAgentAffecter, 'id'>>(
    agentAffecterCollection: Type[],
    ...agentAffectersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agentAffecters: Type[] = agentAffectersToCheck.filter(isPresent);
    if (agentAffecters.length > 0) {
      const agentAffecterCollectionIdentifiers = agentAffecterCollection.map(
        agentAffecterItem => this.getAgentAffecterIdentifier(agentAffecterItem)!
      );
      const agentAffectersToAdd = agentAffecters.filter(agentAffecterItem => {
        const agentAffecterIdentifier = this.getAgentAffecterIdentifier(agentAffecterItem);
        if (agentAffecterCollectionIdentifiers.includes(agentAffecterIdentifier)) {
          return false;
        }
        agentAffecterCollectionIdentifiers.push(agentAffecterIdentifier);
        return true;
      });
      return [...agentAffectersToAdd, ...agentAffecterCollection];
    }
    return agentAffecterCollection;
  }
}
