import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAgentAffecter, NewAgentAffecter } from '../agent-affecter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgentAffecter for edit and NewAgentAffecterFormGroupInput for create.
 */
type AgentAffecterFormGroupInput = IAgentAffecter | PartialWithRequiredKeyOf<NewAgentAffecter>;

type AgentAffecterFormDefaults = Pick<NewAgentAffecter, 'id'>;

type AgentAffecterFormGroupContent = {
  id: FormControl<IAgentAffecter['id'] | NewAgentAffecter['id']>;
  matricule: FormControl<IAgentAffecter['matricule']>;
  nomUtilisateur: FormControl<IAgentAffecter['nomUtilisateur']>;
  prenomUtilisateur: FormControl<IAgentAffecter['prenomUtilisateur']>;
  poste: FormControl<IAgentAffecter['poste']>;
  adresse: FormControl<IAgentAffecter['adresse']>;
  telephone: FormControl<IAgentAffecter['telephone']>;
};

export type AgentAffecterFormGroup = FormGroup<AgentAffecterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgentAffecterFormService {
  createAgentAffecterFormGroup(agentAffecter: AgentAffecterFormGroupInput = { id: null }): AgentAffecterFormGroup {
    const agentAffecterRawValue = {
      ...this.getFormDefaults(),
      ...agentAffecter,
    };
    return new FormGroup<AgentAffecterFormGroupContent>({
      id: new FormControl(
        { value: agentAffecterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      matricule: new FormControl(agentAffecterRawValue.matricule),
      nomUtilisateur: new FormControl(agentAffecterRawValue.nomUtilisateur),
      prenomUtilisateur: new FormControl(agentAffecterRawValue.prenomUtilisateur),
      poste: new FormControl(agentAffecterRawValue.poste),
      adresse: new FormControl(agentAffecterRawValue.adresse),
      telephone: new FormControl(agentAffecterRawValue.telephone),
    });
  }

  getAgentAffecter(form: AgentAffecterFormGroup): IAgentAffecter | NewAgentAffecter {
    return form.getRawValue() as IAgentAffecter | NewAgentAffecter;
  }

  resetForm(form: AgentAffecterFormGroup, agentAffecter: AgentAffecterFormGroupInput): void {
    const agentAffecterRawValue = { ...this.getFormDefaults(), ...agentAffecter };
    form.reset(
      {
        ...agentAffecterRawValue,
        id: { value: agentAffecterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AgentAffecterFormDefaults {
    return {
      id: null,
    };
  }
}
