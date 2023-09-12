import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AgentStructureDetailComponent } from './agent-structure-detail.component';

describe('AgentStructure Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgentStructureDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AgentStructureDetailComponent,
              resolve: { agentStructure: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(AgentStructureDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load agentStructure on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AgentStructureDetailComponent);

      // THEN
      expect(instance.agentStructure).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
