import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ImmobilisationComponent } from './list/immobilisation.component';
import { ImmobilisationDetailComponent } from './detail/immobilisation-detail.component';
import { ImmobilisationUpdateComponent } from './update/immobilisation-update.component';
import ImmobilisationResolve from './route/immobilisation-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const immobilisationRoute: Routes = [
  {
    path: '',
    component: ImmobilisationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ImmobilisationDetailComponent,
    resolve: {
      immobilisation: ImmobilisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ImmobilisationUpdateComponent,
    resolve: {
      immobilisation: ImmobilisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ImmobilisationUpdateComponent,
    resolve: {
      immobilisation: ImmobilisationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default immobilisationRoute;
