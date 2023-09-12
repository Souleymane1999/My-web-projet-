import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AgentAffecterComponent } from './list/agent-affecter.component';
import { AgentAffecterDetailComponent } from './detail/agent-affecter-detail.component';
import { AgentAffecterUpdateComponent } from './update/agent-affecter-update.component';
import AgentAffecterResolve from './route/agent-affecter-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const agentAffecterRoute: Routes = [
  {
    path: '',
    component: AgentAffecterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgentAffecterDetailComponent,
    resolve: {
      agentAffecter: AgentAffecterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgentAffecterUpdateComponent,
    resolve: {
      agentAffecter: AgentAffecterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgentAffecterUpdateComponent,
    resolve: {
      agentAffecter: AgentAffecterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default agentAffecterRoute;
