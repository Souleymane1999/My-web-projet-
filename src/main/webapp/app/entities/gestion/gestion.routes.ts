import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GestionComponent } from './list/gestion.component';
import { GestionDetailComponent } from './detail/gestion-detail.component';
import { GestionUpdateComponent } from './update/gestion-update.component';
import GestionResolve from './route/gestion-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gestionRoute: Routes = [
  {
    path: '',
    component: GestionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GestionDetailComponent,
    resolve: {
      gestion: GestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GestionUpdateComponent,
    resolve: {
      gestion: GestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GestionUpdateComponent,
    resolve: {
      gestion: GestionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default gestionRoute;
