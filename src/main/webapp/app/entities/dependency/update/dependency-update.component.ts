import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubcontent } from 'app/entities/subcontent/subcontent.model';
import { SubcontentService } from 'app/entities/subcontent/service/subcontent.service';
import { IDependency } from '../dependency.model';
import { DependencyService } from '../service/dependency.service';
import { DependencyFormGroup, DependencyFormService } from './dependency-form.service';

@Component({
  selector: 'jhi-dependency-update',
  templateUrl: './dependency-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DependencyUpdateComponent implements OnInit {
  isSaving = false;
  dependency: IDependency | null = null;

  subcontentsSharedCollection: ISubcontent[] = [];

  protected dependencyService = inject(DependencyService);
  protected dependencyFormService = inject(DependencyFormService);
  protected subcontentService = inject(SubcontentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DependencyFormGroup = this.dependencyFormService.createDependencyFormGroup();

  compareSubcontent = (o1: ISubcontent | null, o2: ISubcontent | null): boolean => this.subcontentService.compareSubcontent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dependency }) => {
      this.dependency = dependency;
      if (dependency) {
        this.updateForm(dependency);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dependency = this.dependencyFormService.getDependency(this.editForm);
    if (dependency.id !== null) {
      this.subscribeToSaveResponse(this.dependencyService.update(dependency));
    } else {
      this.subscribeToSaveResponse(this.dependencyService.create(dependency));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDependency>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(dependency: IDependency): void {
    this.dependency = dependency;
    this.dependencyFormService.resetForm(this.editForm, dependency);

    this.subcontentsSharedCollection = this.subcontentService.addSubcontentToCollectionIfMissing<ISubcontent>(
      this.subcontentsSharedCollection,
      dependency.subcontent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subcontentService
      .query()
      .pipe(map((res: HttpResponse<ISubcontent[]>) => res.body ?? []))
      .pipe(
        map((subcontents: ISubcontent[]) =>
          this.subcontentService.addSubcontentToCollectionIfMissing<ISubcontent>(subcontents, this.dependency?.subcontent),
        ),
      )
      .subscribe((subcontents: ISubcontent[]) => (this.subcontentsSharedCollection = subcontents));
  }
}
