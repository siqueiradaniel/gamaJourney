import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ISubcontent } from '../subcontent.model';

@Component({
  selector: 'jhi-subcontent-detail',
  templateUrl: './subcontent-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class SubcontentDetailComponent {
  subcontent = input<ISubcontent | null>(null);

  previousState(): void {
    window.history.back();
  }
}
