import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDependency } from '../dependency.model';
import { DependencyService } from '../service/dependency.service';

@Component({
  templateUrl: './dependency-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DependencyDeleteDialogComponent {
  dependency?: IDependency;

  protected dependencyService = inject(DependencyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dependencyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
