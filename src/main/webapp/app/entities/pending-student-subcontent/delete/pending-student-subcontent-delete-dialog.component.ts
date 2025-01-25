import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPendingStudentSubcontent } from '../pending-student-subcontent.model';
import { PendingStudentSubcontentService } from '../service/pending-student-subcontent.service';

@Component({
  templateUrl: './pending-student-subcontent-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PendingStudentSubcontentDeleteDialogComponent {
  pendingStudentSubcontent?: IPendingStudentSubcontent;

  protected pendingStudentSubcontentService = inject(PendingStudentSubcontentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pendingStudentSubcontentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
