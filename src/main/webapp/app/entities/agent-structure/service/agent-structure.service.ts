import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAgentStructure, NewAgentStructure } from '../agent-structure.model';

export type PartialUpdateAgentStructure = Partial<IAgentStructure> & Pick<IAgentStructure, 'id'>;

export type EntityResponseType = HttpResponse<IAgentStructure>;
export type EntityArrayResponseType = HttpResponse<IAgentStructure[]>;

@Injectable({ providedIn: 'root' })
export class AgentStructureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/agent-structures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(agentStructure: NewAgentStructure): Observable<EntityResponseType> {
    return this.http.post<IAgentStructure>(this.resourceUrl, agentStructure, { observe: 'response' });
  }

  update(agentStructure: IAgentStructure): Observable<EntityResponseType> {
    return this.http.put<IAgentStructure>(`${this.resourceUrl}/${this.getAgentStructureIdentifier(agentStructure)}`, agentStructure, {
      observe: 'response',
    });
  }

  partialUpdate(agentStructure: PartialUpdateAgentStructure): Observable<EntityResponseType> {
    return this.http.patch<IAgentStructure>(`${this.resourceUrl}/${this.getAgentStructureIdentifier(agentStructure)}`, agentStructure, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAgentStructure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAgentStructure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAgentStructureIdentifier(agentStructure: Pick<IAgentStructure, 'id'>): number {
    return agentStructure.id;
  }

  compareAgentStructure(o1: Pick<IAgentStructure, 'id'> | null, o2: Pick<IAgentStructure, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgentStructureIdentifier(o1) === this.getAgentStructureIdentifier(o2) : o1 === o2;
  }

  addAgentStructureToCollectionIfMissing<Type extends Pick<IAgentStructure, 'id'>>(
    agentStructureCollection: Type[],
    ...agentStructuresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agentStructures: Type[] = agentStructuresToCheck.filter(isPresent);
    if (agentStructures.length > 0) {
      const agentStructureCollectionIdentifiers = agentStructureCollection.map(
        agentStructureItem => this.getAgentStructureIdentifier(agentStructureItem)!
      );
      const agentStructuresToAdd = agentStructures.filter(agentStructureItem => {
        const agentStructureIdentifier = this.getAgentStructureIdentifier(agentStructureItem);
        if (agentStructureCollectionIdentifiers.includes(agentStructureIdentifier)) {
          return false;
        }
        agentStructureCollectionIdentifiers.push(agentStructureIdentifier);
        return true;
      });
      return [...agentStructuresToAdd, ...agentStructureCollection];
    }
    return agentStructureCollection;
  }
}
