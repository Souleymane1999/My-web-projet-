import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MaintenanceComponent } from './list/maintenance.component';
import { MaintenanceDetailComponent } from './detail/maintenance-detail.component';
import { MaintenanceUpdateComponent } from './update/maintenance-update.component';
import MaintenanceResolve from './route/maintenance-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const maintenanceRoute: Routes = [
  {
    path: '',
    component: MaintenanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MaintenanceDetailComponent,
    resolve: {
      maintenance: MaintenanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MaintenanceUpdateComponent,
    resolve: {
      maintenance: MaintenanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MaintenanceUpdateComponent,
    resolve: {
      maintenance: MaintenanceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default maintenanceRoute;
