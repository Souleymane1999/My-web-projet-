import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TransfertComponent } from './list/transfert.component';
import { TransfertDetailComponent } from './detail/transfert-detail.component';
import { TransfertUpdateComponent } from './update/transfert-update.component';
import TransfertResolve from './route/transfert-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const transfertRoute: Routes = [
  {
    path: '',
    component: TransfertComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransfertDetailComponent,
    resolve: {
      transfert: TransfertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransfertUpdateComponent,
    resolve: {
      transfert: TransfertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransfertUpdateComponent,
    resolve: {
      transfert: TransfertResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transfertRoute;
