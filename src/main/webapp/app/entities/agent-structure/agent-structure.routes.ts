import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AgentStructureComponent } from './list/agent-structure.component';
import { AgentStructureDetailComponent } from './detail/agent-structure-detail.component';
import { AgentStructureUpdateComponent } from './update/agent-structure-update.component';
import AgentStructureResolve from './route/agent-structure-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const agentStructureRoute: Routes = [
  {
    path: '',
    component: AgentStructureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AgentStructureDetailComponent,
    resolve: {
      agentStructure: AgentStructureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AgentStructureUpdateComponent,
    resolve: {
      agentStructure: AgentStructureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AgentStructureUpdateComponent,
    resolve: {
      agentStructure: AgentStructureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default agentStructureRoute;
