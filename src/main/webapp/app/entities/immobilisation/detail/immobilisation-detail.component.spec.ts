import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ImmobilisationDetailComponent } from './immobilisation-detail.component';

describe('Immobilisation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ImmobilisationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ImmobilisationDetailComponent,
              resolve: { immobilisation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ImmobilisationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load immobilisation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ImmobilisationDetailComponent);

      // THEN
      expect(instance.immobilisation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
