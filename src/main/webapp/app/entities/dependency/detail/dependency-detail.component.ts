import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDependency } from '../dependency.model';

@Component({
  selector: 'jhi-dependency-detail',
  templateUrl: './dependency-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DependencyDetailComponent {
  dependency = input<IDependency | null>(null);

  previousState(): void {
    window.history.back();
  }
}
