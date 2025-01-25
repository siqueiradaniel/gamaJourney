import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContent } from 'app/entities/content/content.model';
import { ContentService } from 'app/entities/content/service/content.service';
import { ISubcontent } from '../subcontent.model';
import { SubcontentService } from '../service/subcontent.service';
import { SubcontentFormGroup, SubcontentFormService } from './subcontent-form.service';

@Component({
  selector: 'jhi-subcontent-update',
  templateUrl: './subcontent-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubcontentUpdateComponent implements OnInit {
  isSaving = false;
  subcontent: ISubcontent | null = null;

  contentsSharedCollection: IContent[] = [];

  protected subcontentService = inject(SubcontentService);
  protected subcontentFormService = inject(SubcontentFormService);
  protected contentService = inject(ContentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubcontentFormGroup = this.subcontentFormService.createSubcontentFormGroup();

  compareContent = (o1: IContent | null, o2: IContent | null): boolean => this.contentService.compareContent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subcontent }) => {
      this.subcontent = subcontent;
      if (subcontent) {
        this.updateForm(subcontent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subcontent = this.subcontentFormService.getSubcontent(this.editForm);
    if (subcontent.id !== null) {
      this.subscribeToSaveResponse(this.subcontentService.update(subcontent));
    } else {
      this.subscribeToSaveResponse(this.subcontentService.create(subcontent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubcontent>>): void {
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

  protected updateForm(subcontent: ISubcontent): void {
    this.subcontent = subcontent;
    this.subcontentFormService.resetForm(this.editForm, subcontent);

    this.contentsSharedCollection = this.contentService.addContentToCollectionIfMissing<IContent>(
      this.contentsSharedCollection,
      subcontent.content,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contentService
      .query()
      .pipe(map((res: HttpResponse<IContent[]>) => res.body ?? []))
      .pipe(
        map((contents: IContent[]) => this.contentService.addContentToCollectionIfMissing<IContent>(contents, this.subcontent?.content)),
      )
      .subscribe((contents: IContent[]) => (this.contentsSharedCollection = contents));
  }
}
