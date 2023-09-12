import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgentAffecterDetailComponent } from './agent-affecter-detail.component';

describe('AgentAffecter Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentAffecterDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AgentAffecterDetailComponent,
              resolve: { agentAffecter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(AgentAffecterDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load agentAffecter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AgentAffecterDetailComponent);

      // THEN
      expect(instance.agentAffecter).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
