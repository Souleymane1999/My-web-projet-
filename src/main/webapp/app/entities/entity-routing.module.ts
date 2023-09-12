import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'agent-affecter',
        data: { pageTitle: 'AgentAffecters' },
        loadChildren: () => import('./agent-affecter/agent-affecter.routes'),
      },
      {
        path: 'agent-structure',
        data: { pageTitle: 'AgentStructures' },
        loadChildren: () => import('./agent-structure/agent-structure.routes'),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./categorie/categorie.routes'),
      },
      {
        path: 'gestion',
        data: { pageTitle: 'Gestions' },
        loadChildren: () => import('./gestion/gestion.routes'),
      },
      {
        path: 'immobilisation',
        data: { pageTitle: 'Immobilisations' },
        loadChildren: () => import('./immobilisation/immobilisation.routes'),
      },
      {
        path: 'localite',
        data: { pageTitle: 'Localites' },
        loadChildren: () => import('./localite/localite.routes'),
      },
      {
        path: 'maintenance',
        data: { pageTitle: 'Maintenances' },
        loadChildren: () => import('./maintenance/maintenance.routes'),
      },
      {
        path: 'structure',
        data: { pageTitle: 'Structures' },
        loadChildren: () => import('./structure/structure.routes'),
      },
      {
        path: 'transfert',
        data: { pageTitle: 'Transferts' },
        loadChildren: () => import('./transfert/transfert.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
