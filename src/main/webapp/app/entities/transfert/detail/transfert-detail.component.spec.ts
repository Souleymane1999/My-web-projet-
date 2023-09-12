import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TransfertDetailComponent } from './transfert-detail.component';

describe('Transfert Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransfertDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TransfertDetailComponent,
              resolve: { transfert: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(TransfertDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load transfert on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TransfertDetailComponent);

      // THEN
      expect(instance.transfert).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
