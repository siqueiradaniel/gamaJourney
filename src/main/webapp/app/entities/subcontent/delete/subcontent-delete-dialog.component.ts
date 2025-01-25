import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISubcontent } from '../subcontent.model';
import { SubcontentService } from '../service/subcontent.service';

@Component({
  templateUrl: './subcontent-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SubcontentDeleteDialogComponent {
  subcontent?: ISubcontent;

  protected subcontentService = inject(SubcontentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.subcontentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
