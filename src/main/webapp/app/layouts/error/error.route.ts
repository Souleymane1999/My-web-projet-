import { Routes } from '@angular/router';

import ErrorComponent from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    title: 'Page erreur !',
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      errorMessage: 'Vous navez pas les droits pour accéder à cette page.',
    },
    title: 'Page derreur !',
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      errorMessage: 'La page nexiste pas.',
    },
    title: 'Page derreur !',
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
