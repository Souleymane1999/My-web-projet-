import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAgent, NewAgent } from '../agent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgent for edit and NewAgentFormGroupInput for create.
 */
type AgentFormGroupInput = IAgent | PartialWithRequiredKeyOf<NewAgent>;

type AgentFormDefaults = Pick<NewAgent, 'id'>;

type AgentFormGroupContent = {
  id: FormControl<IAgent['id'] | NewAgent['id']>;
  matricule: FormControl<IAgent['matricule']>;
  nomUtilisateur: FormControl<IAgent['nomUtilisateur']>;
  prenomUtilisateur: FormControl<IAgent['prenomUtilisateur']>;
  poste: FormControl<IAgent['poste']>;
  adresse: FormControl<IAgent['adresse']>;
  telephone: FormControl<IAgent['telephone']>;
};

export type AgentFormGroup = FormGroup<AgentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgentFormService {
  createAgentFormGroup(agent: AgentFormGroupInput = { id: null }): AgentFormGroup {
    const agentRawValue = {
      ...this.getFormDefaults(),
      ...agent,
    };
    return new FormGroup<AgentFormGroupContent>({
      id: new FormControl(
        { value: agentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      matricule: new FormControl(agentRawValue.matricule),
      nomUtilisateur: new FormControl(agentRawValue.nomUtilisateur),
      prenomUtilisateur: new FormControl(agentRawValue.prenomUtilisateur),
      poste: new FormControl(agentRawValue.poste),
      adresse: new FormControl(agentRawValue.adresse),
      telephone: new FormControl(agentRawValue.telephone),
    });
  }

  getAgent(form: AgentFormGroup): IAgent | NewAgent {
    return form.getRawValue() as IAgent | NewAgent;
  }

  resetForm(form: AgentFormGroup, agent: AgentFormGroupInput): void {
    const agentRawValue = { ...this.getFormDefaults(), ...agent };
    form.reset(
      {
        ...agentRawValue,
        id: { value: agentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AgentFormDefaults {
    return {
      id: null,
    };
  }
}
