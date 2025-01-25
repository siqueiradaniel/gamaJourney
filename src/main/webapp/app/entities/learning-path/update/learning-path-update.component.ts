import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { ISubcontent } from 'app/entities/subcontent/subcontent.model';
import { SubcontentService } from 'app/entities/subcontent/service/subcontent.service';
import { LearningPathService } from '../service/learning-path.service';
import { ILearningPath } from '../learning-path.model';
import { LearningPathFormGroup, LearningPathFormService } from './learning-path-form.service';

@Component({
  selector: 'jhi-learning-path-update',
  templateUrl: './learning-path-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LearningPathUpdateComponent implements OnInit {
  isSaving = false;
  learningPath: ILearningPath | null = null;

  examsSharedCollection: IExam[] = [];
  subcontentsSharedCollection: ISubcontent[] = [];

  protected learningPathService = inject(LearningPathService);
  protected learningPathFormService = inject(LearningPathFormService);
  protected examService = inject(ExamService);
  protected subcontentService = inject(SubcontentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LearningPathFormGroup = this.learningPathFormService.createLearningPathFormGroup();

  compareExam = (o1: IExam | null, o2: IExam | null): boolean => this.examService.compareExam(o1, o2);

  compareSubcontent = (o1: ISubcontent | null, o2: ISubcontent | null): boolean => this.subcontentService.compareSubcontent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ learningPath }) => {
      this.learningPath = learningPath;
      if (learningPath) {
        this.updateForm(learningPath);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const learningPath = this.learningPathFormService.getLearningPath(this.editForm);
    if (learningPath.id !== null) {
      this.subscribeToSaveResponse(this.learningPathService.update(learningPath));
    } else {
      this.subscribeToSaveResponse(this.learningPathService.create(learningPath));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILearningPath>>): void {
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

  protected updateForm(learningPath: ILearningPath): void {
    this.learningPath = learningPath;
    this.learningPathFormService.resetForm(this.editForm, learningPath);

    this.examsSharedCollection = this.examService.addExamToCollectionIfMissing<IExam>(this.examsSharedCollection, learningPath.exam);
    this.subcontentsSharedCollection = this.subcontentService.addSubcontentToCollectionIfMissing<ISubcontent>(
      this.subcontentsSharedCollection,
      learningPath.subcontent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.examService
      .query()
      .pipe(map((res: HttpResponse<IExam[]>) => res.body ?? []))
      .pipe(map((exams: IExam[]) => this.examService.addExamToCollectionIfMissing<IExam>(exams, this.learningPath?.exam)))
      .subscribe((exams: IExam[]) => (this.examsSharedCollection = exams));

    this.subcontentService
      .query()
      .pipe(map((res: HttpResponse<ISubcontent[]>) => res.body ?? []))
      .pipe(
        map((subcontents: ISubcontent[]) =>
          this.subcontentService.addSubcontentToCollectionIfMissing<ISubcontent>(subcontents, this.learningPath?.subcontent),
        ),
      )
      .subscribe((subcontents: ISubcontent[]) => (this.subcontentsSharedCollection = subcontents));
  }
}
