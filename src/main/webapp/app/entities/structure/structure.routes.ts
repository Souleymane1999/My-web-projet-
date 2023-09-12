import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StructureComponent } from './list/structure.component';
import { StructureDetailComponent } from './detail/structure-detail.component';
import { StructureUpdateComponent } from './update/structure-update.component';
import StructureResolve from './route/structure-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const structureRoute: Routes = [
  {
    path: '',
    component: StructureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StructureDetailComponent,
    resolve: {
      structure: StructureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StructureUpdateComponent,
    resolve: {
      structure: StructureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StructureUpdateComponent,
    resolve: {
      structure: StructureResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default structureRoute;
