import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IAgentAffecter } from '../agent-affecter.model';
import { AgentAffecterService } from '../service/agent-affecter.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './agent-affecter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AgentAffecterDeleteDialogComponent {
  agentAffecter?: IAgentAffecter;

  constructor(protected agentAffecterService: AgentAffecterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.agentAffecterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
