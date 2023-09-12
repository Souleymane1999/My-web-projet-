import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImmobilisation } from '../immobilisation.model';
import { ImmobilisationService } from '../service/immobilisation.service';

export const immobilisationResolve = (route: ActivatedRouteSnapshot): Observable<null | IImmobilisation> => {
  const id = route.params['id'];
  if (id) {
    return inject(ImmobilisationService)
      .find(id)
      .pipe(
        mergeMap((immobilisation: HttpResponse<IImmobilisation>) => {
          if (immobilisation.body) {
            return of(immobilisation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default immobilisationResolve;
