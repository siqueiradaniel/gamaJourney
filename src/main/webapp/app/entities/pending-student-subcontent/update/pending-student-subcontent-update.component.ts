import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubcontent } from 'app/entities/subcontent/subcontent.model';
import { SubcontentService } from 'app/entities/subcontent/service/subcontent.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { PendingStudentSubcontentService } from '../service/pending-student-subcontent.service';
import { IPendingStudentSubcontent } from '../pending-student-subcontent.model';
import { PendingStudentSubcontentFormGroup, PendingStudentSubcontentFormService } from './pending-student-subcontent-form.service';

@Component({
  selector: 'jhi-pending-student-subcontent-update',
  templateUrl: './pending-student-subcontent-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PendingStudentSubcontentUpdateComponent implements OnInit {
  isSaving = false;
  pendingStudentSubcontent: IPendingStudentSubcontent | null = null;

  subcontentsSharedCollection: ISubcontent[] = [];
  studentsSharedCollection: IStudent[] = [];

  protected pendingStudentSubcontentService = inject(PendingStudentSubcontentService);
  protected pendingStudentSubcontentFormService = inject(PendingStudentSubcontentFormService);
  protected subcontentService = inject(SubcontentService);
  protected studentService = inject(StudentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PendingStudentSubcontentFormGroup = this.pendingStudentSubcontentFormService.createPendingStudentSubcontentFormGroup();

  compareSubcontent = (o1: ISubcontent | null, o2: ISubcontent | null): boolean => this.subcontentService.compareSubcontent(o1, o2);

  compareStudent = (o1: IStudent | null, o2: IStudent | null): boolean => this.studentService.compareStudent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pendingStudentSubcontent }) => {
      this.pendingStudentSubcontent = pendingStudentSubcontent;
      if (pendingStudentSubcontent) {
        this.updateForm(pendingStudentSubcontent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pendingStudentSubcontent = this.pendingStudentSubcontentFormService.getPendingStudentSubcontent(this.editForm);
    if (pendingStudentSubcontent.id !== null) {
      this.subscribeToSaveResponse(this.pendingStudentSubcontentService.update(pendingStudentSubcontent));
    } else {
      this.subscribeToSaveResponse(this.pendingStudentSubcontentService.create(pendingStudentSubcontent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPendingStudentSubcontent>>): void {
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

  protected updateForm(pendingStudentSubcontent: IPendingStudentSubcontent): void {
    this.pendingStudentSubcontent = pendingStudentSubcontent;
    this.pendingStudentSubcontentFormService.resetForm(this.editForm, pendingStudentSubcontent);

    this.subcontentsSharedCollection = this.subcontentService.addSubcontentToCollectionIfMissing<ISubcontent>(
      this.subcontentsSharedCollection,
      pendingStudentSubcontent.subcontent,
    );
    this.studentsSharedCollection = this.studentService.addStudentToCollectionIfMissing<IStudent>(
      this.studentsSharedCollection,
      pendingStudentSubcontent.student,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subcontentService
      .query()
      .pipe(map((res: HttpResponse<ISubcontent[]>) => res.body ?? []))
      .pipe(
        map((subcontents: ISubcontent[]) =>
          this.subcontentService.addSubcontentToCollectionIfMissing<ISubcontent>(subcontents, this.pendingStudentSubcontent?.subcontent),
        ),
      )
      .subscribe((subcontents: ISubcontent[]) => (this.subcontentsSharedCollection = subcontents));

    this.studentService
      .query()
      .pipe(map((res: HttpResponse<IStudent[]>) => res.body ?? []))
      .pipe(
        map((students: IStudent[]) =>
          this.studentService.addStudentToCollectionIfMissing<IStudent>(students, this.pendingStudentSubcontent?.student),
        ),
      )
      .subscribe((students: IStudent[]) => (this.studentsSharedCollection = students));
  }
}
