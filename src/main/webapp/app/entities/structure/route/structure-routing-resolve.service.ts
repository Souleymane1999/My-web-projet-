import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStructure } from '../structure.model';
import { StructureService } from '../service/structure.service';

export const structureResolve = (route: ActivatedRouteSnapshot): Observable<null | IStructure> => {
  const id = route.params['id'];
  if (id) {
    return inject(StructureService)
      .find(id)
      .pipe(
        mergeMap((structure: HttpResponse<IStructure>) => {
          if (structure.body) {
            return of(structure.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default structureResolve;
