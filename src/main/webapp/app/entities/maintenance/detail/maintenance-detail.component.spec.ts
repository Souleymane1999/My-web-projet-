import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MaintenanceDetailComponent } from './maintenance-detail.component';

describe('Maintenance Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaintenanceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MaintenanceDetailComponent,
              resolve: { maintenance: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(MaintenanceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load maintenance on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MaintenanceDetailComponent);

      // THEN
      expect(instance.maintenance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
