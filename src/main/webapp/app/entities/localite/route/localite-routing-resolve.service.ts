import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocalite } from '../localite.model';
import { LocaliteService } from '../service/localite.service';

export const localiteResolve = (route: ActivatedRouteSnapshot): Observable<null | ILocalite> => {
  const id = route.params['id'];
  if (id) {
    return inject(LocaliteService)
      .find(id)
      .pipe(
        mergeMap((localite: HttpResponse<ILocalite>) => {
          if (localite.body) {
            return of(localite.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default localiteResolve;
