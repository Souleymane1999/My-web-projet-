import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgentStructure } from '../agent-structure.model';
import { AgentStructureService } from '../service/agent-structure.service';

export const agentStructureResolve = (route: ActivatedRouteSnapshot): Observable<null | IAgentStructure> => {
  const id = route.params['id'];
  if (id) {
    return inject(AgentStructureService)
      .find(id)
      .pipe(
        mergeMap((agentStructure: HttpResponse<IAgentStructure>) => {
          if (agentStructure.body) {
            return of(agentStructure.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default agentStructureResolve;
