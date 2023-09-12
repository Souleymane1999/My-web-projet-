import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { GestionDetailComponent } from './gestion-detail.component';

describe('Gestion Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: GestionDetailComponent,
              resolve: { gestion: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(GestionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load gestion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GestionDetailComponent);

      // THEN
      expect(instance.gestion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
