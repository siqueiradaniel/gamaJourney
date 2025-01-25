import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IContent } from '../content.model';

@Component({
  selector: 'jhi-content-detail',
  templateUrl: './content-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ContentDetailComponent {
  content = input<IContent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
