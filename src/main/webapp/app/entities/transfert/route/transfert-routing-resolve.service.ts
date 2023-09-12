import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransfert } from '../transfert.model';
import { TransfertService } from '../service/transfert.service';

export const transfertResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransfert> => {
  const id = route.params['id'];
  if (id) {
    return inject(TransfertService)
      .find(id)
      .pipe(
        mergeMap((transfert: HttpResponse<ITransfert>) => {
          if (transfert.body) {
            return of(transfert.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default transfertResolve;
