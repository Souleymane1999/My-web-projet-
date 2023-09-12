import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { StructureDetailComponent } from './structure-detail.component';

describe('Structure Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StructureDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: StructureDetailComponent,
              resolve: { structure: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(StructureDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load structure on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', StructureDetailComponent);

      // THEN
      expect(instance.structure).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
