import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaintenance } from '../maintenance.model';
import { MaintenanceService } from '../service/maintenance.service';

export const maintenanceResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaintenance> => {
  const id = route.params['id'];
  if (id) {
    return inject(MaintenanceService)
      .find(id)
      .pipe(
        mergeMap((maintenance: HttpResponse<IMaintenance>) => {
          if (maintenance.body) {
            return of(maintenance.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default maintenanceResolve;
