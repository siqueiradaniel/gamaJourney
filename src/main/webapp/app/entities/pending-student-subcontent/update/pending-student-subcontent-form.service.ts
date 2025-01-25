import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPendingStudentSubcontent, NewPendingStudentSubcontent } from '../pending-student-subcontent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPendingStudentSubcontent for edit and NewPendingStudentSubcontentFormGroupInput for create.
 */
type PendingStudentSubcontentFormGroupInput = IPendingStudentSubcontent | PartialWithRequiredKeyOf<NewPendingStudentSubcontent>;

type PendingStudentSubcontentFormDefaults = Pick<NewPendingStudentSubcontent, 'id'>;

type PendingStudentSubcontentFormGroupContent = {
  id: FormControl<IPendingStudentSubcontent['id'] | NewPendingStudentSubcontent['id']>;
  studentId: FormControl<IPendingStudentSubcontent['studentId']>;
  subcontentId: FormControl<IPendingStudentSubcontent['subcontentId']>;
  currentStatus: FormControl<IPendingStudentSubcontent['currentStatus']>;
  subcontent: FormControl<IPendingStudentSubcontent['subcontent']>;
  student: FormControl<IPendingStudentSubcontent['student']>;
};

export type PendingStudentSubcontentFormGroup = FormGroup<PendingStudentSubcontentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PendingStudentSubcontentFormService {
  createPendingStudentSubcontentFormGroup(
    pendingStudentSubcontent: PendingStudentSubcontentFormGroupInput = { id: null },
  ): PendingStudentSubcontentFormGroup {
    const pendingStudentSubcontentRawValue = {
      ...this.getFormDefaults(),
      ...pendingStudentSubcontent,
    };
    return new FormGroup<PendingStudentSubcontentFormGroupContent>({
      id: new FormControl(
        { value: pendingStudentSubcontentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      studentId: new FormControl(pendingStudentSubcontentRawValue.studentId),
      subcontentId: new FormControl(pendingStudentSubcontentRawValue.subcontentId),
      currentStatus: new FormControl(pendingStudentSubcontentRawValue.currentStatus, {
        validators: [Validators.required],
      }),
      subcontent: new FormControl(pendingStudentSubcontentRawValue.subcontent),
      student: new FormControl(pendingStudentSubcontentRawValue.student),
    });
  }

  getPendingStudentSubcontent(form: PendingStudentSubcontentFormGroup): IPendingStudentSubcontent | NewPendingStudentSubcontent {
    return form.getRawValue() as IPendingStudentSubcontent | NewPendingStudentSubcontent;
  }

  resetForm(form: PendingStudentSubcontentFormGroup, pendingStudentSubcontent: PendingStudentSubcontentFormGroupInput): void {
    const pendingStudentSubcontentRawValue = { ...this.getFormDefaults(), ...pendingStudentSubcontent };
    form.reset(
      {
        ...pendingStudentSubcontentRawValue,
        id: { value: pendingStudentSubcontentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PendingStudentSubcontentFormDefaults {
    return {
      id: null,
    };
  }
}
