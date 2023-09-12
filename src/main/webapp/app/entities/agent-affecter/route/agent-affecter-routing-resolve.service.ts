import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAgentAffecter } from '../agent-affecter.model';
import { AgentAffecterService } from '../service/agent-affecter.service';

export const agentAffecterResolve = (route: ActivatedRouteSnapshot): Observable<null | IAgentAffecter> => {
  const id = route.params['id'];
  if (id) {
    return inject(AgentAffecterService)
      .find(id)
      .pipe(
        mergeMap((agentAffecter: HttpResponse<IAgentAffecter>) => {
          if (agentAffecter.body) {
            return of(agentAffecter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default agentAffecterResolve;
