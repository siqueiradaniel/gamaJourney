import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPendingStudentSubcontent } from '../pending-student-subcontent.model';

@Component({
  selector: 'jhi-pending-student-subcontent-detail',
  templateUrl: './pending-student-subcontent-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PendingStudentSubcontentDetailComponent {
  pendingStudentSubcontent = input<IPendingStudentSubcontent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
