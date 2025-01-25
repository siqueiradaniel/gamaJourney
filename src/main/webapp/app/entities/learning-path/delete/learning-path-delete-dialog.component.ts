import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILearningPath } from '../learning-path.model';
import { LearningPathService } from '../service/learning-path.service';

@Component({
  templateUrl: './learning-path-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LearningPathDeleteDialogComponent {
  learningPath?: ILearningPath;

  protected learningPathService = inject(LearningPathService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.learningPathService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
