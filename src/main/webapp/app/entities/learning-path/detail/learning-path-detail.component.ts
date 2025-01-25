import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ILearningPath } from '../learning-path.model';

@Component({
  selector: 'jhi-learning-path-detail',
  templateUrl: './learning-path-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class LearningPathDetailComponent {
  learningPath = input<ILearningPath | null>(null);

  previousState(): void {
    window.history.back();
  }
}
