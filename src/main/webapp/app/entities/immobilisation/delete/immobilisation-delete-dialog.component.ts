import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IImmobilisation } from '../immobilisation.model';
import { ImmobilisationService } from '../service/immobilisation.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './immobilisation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImmobilisationDeleteDialogComponent {
  immobilisation?: IImmobilisation;

  constructor(protected immobilisationService: ImmobilisationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.immobilisationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
