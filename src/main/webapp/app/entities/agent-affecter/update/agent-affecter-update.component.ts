import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AgentAffecterFormService, AgentAffecterFormGroup } from './agent-affecter-form.service';
import { IAgentAffecter } from '../agent-affecter.model';
import { AgentAffecterService } from '../service/agent-affecter.service';

@Component({
  standalone: true,
  selector: 'jhi-agent-affecter-update',
  templateUrl: './agent-affecter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgentAffecterUpdateComponent implements OnInit {
  isSaving = false;
  agentAffecter: IAgentAffecter | null = null;

  editForm: AgentAffecterFormGroup = this.agentAffecterFormService.createAgentAffecterFormGroup();

  constructor(
    protected agentAffecterService: AgentAffecterService,
    protected agentAffecterFormService: AgentAffecterFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agentAffecter }) => {
      this.agentAffecter = agentAffecter;
      if (agentAffecter) {
        this.updateForm(agentAffecter);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agentAffecter = this.agentAffecterFormService.getAgentAffecter(this.editForm);
    if (agentAffecter.id !== null) {
      this.subscribeToSaveResponse(this.agentAffecterService.update(agentAffecter));
    } else {
      this.subscribeToSaveResponse(this.agentAffecterService.create(agentAffecter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgentAffecter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(agentAffecter: IAgentAffecter): void {
    this.agentAffecter = agentAffecter;
    this.agentAffecterFormService.resetForm(this.editForm, agentAffecter);
  }
}
