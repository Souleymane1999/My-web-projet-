import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocaliteComponent } from './list/localite.component';
import { LocaliteDetailComponent } from './detail/localite-detail.component';
import { LocaliteUpdateComponent } from './update/localite-update.component';
import LocaliteResolve from './route/localite-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const localiteRoute: Routes = [
  {
    path: '',
    component: LocaliteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocaliteDetailComponent,
    resolve: {
      localite: LocaliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocaliteUpdateComponent,
    resolve: {
      localite: LocaliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocaliteUpdateComponent,
    resolve: {
      localite: LocaliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default localiteRoute;
