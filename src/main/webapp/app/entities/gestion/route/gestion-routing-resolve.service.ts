import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGestion } from '../gestion.model';
import { GestionService } from '../service/gestion.service';

export const gestionResolve = (route: ActivatedRouteSnapshot): Observable<null | IGestion> => {
  const id = route.params['id'];
  if (id) {
    return inject(GestionService)
      .find(id)
      .pipe(
        mergeMap((gestion: HttpResponse<IGestion>) => {
          if (gestion.body) {
            return of(gestion.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default gestionResolve;
