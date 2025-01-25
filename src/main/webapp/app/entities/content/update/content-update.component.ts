import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IContent } from '../content.model';
import { ContentService } from '../service/content.service';
import { ContentFormGroup, ContentFormService } from './content-form.service';

@Component({
  selector: 'jhi-content-update',
  templateUrl: './content-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ContentUpdateComponent implements OnInit {
  isSaving = false;
  content: IContent | null = null;

  protected contentService = inject(ContentService);
  protected contentFormService = inject(ContentFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContentFormGroup = this.contentFormService.createContentFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ content }) => {
      this.content = content;
      if (content) {
        this.updateForm(content);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const content = this.contentFormService.getContent(this.editForm);
    if (content.id !== null) {
      this.subscribeToSaveResponse(this.contentService.update(content));
    } else {
      this.subscribeToSaveResponse(this.contentService.create(content));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContent>>): void {
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

  protected updateForm(content: IContent): void {
    this.content = content;
    this.contentFormService.resetForm(this.editForm, content);
  }
}
