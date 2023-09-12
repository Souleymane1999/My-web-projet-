import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAgentStructure, NewAgentStructure } from '../agent-structure.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgentStructure for edit and NewAgentStructureFormGroupInput for create.
 */
type AgentStructureFormGroupInput = IAgentStructure | PartialWithRequiredKeyOf<NewAgentStructure>;

type AgentStructureFormDefaults = Pick<NewAgentStructure, 'id'>;

type AgentStructureFormGroupContent = {
  id: FormControl<IAgentStructure['id'] | NewAgentStructure['id']>;
  struture: FormControl<IAgentStructure['struture']>;
  agent: FormControl<IAgentStructure['agent']>;
};

export type AgentStructureFormGroup = FormGroup<AgentStructureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgentStructureFormService {
  createAgentStructureFormGroup(agentStructure: AgentStructureFormGroupInput = { id: null }): AgentStructureFormGroup {
    const agentStructureRawValue = {
      ...this.getFormDefaults(),
      ...agentStructure,
    };
    return new FormGroup<AgentStructureFormGroupContent>({
      id: new FormControl(
        { value: agentStructureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      struture: new FormControl(agentStructureRawValue.struture),
      agent: new FormControl(agentStructureRawValue.agent),
    });
  }

  getAgentStructure(form: AgentStructureFormGroup): IAgentStructure | NewAgentStructure {
    return form.getRawValue() as IAgentStructure | NewAgentStructure;
  }

  resetForm(form: AgentStructureFormGroup, agentStructure: AgentStructureFormGroupInput): void {
    const agentStructureRawValue = { ...this.getFormDefaults(), ...agentStructure };
    form.reset(
      {
        ...agentStructureRawValue,
        id: { value: agentStructureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AgentStructureFormDefaults {
    return {
      id: null,
    };
  }
}
