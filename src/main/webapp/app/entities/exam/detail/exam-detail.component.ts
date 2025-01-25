import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IExam } from '../exam.model';

@Component({
  selector: 'jhi-exam-detail',
  templateUrl: './exam-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ExamDetailComponent {
  exam = input<IExam | null>(null);

  previousState(): void {
    window.history.back();
  }
}
