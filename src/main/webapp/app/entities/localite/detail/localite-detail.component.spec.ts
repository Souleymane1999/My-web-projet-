import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LocaliteDetailComponent } from './localite-detail.component';

describe('Localite Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LocaliteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LocaliteDetailComponent,
              resolve: { localite: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(LocaliteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load localite on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LocaliteDetailComponent);

      // THEN
      expect(instance.localite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
