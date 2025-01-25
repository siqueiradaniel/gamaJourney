import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILearningPath, NewLearningPath } from '../learning-path.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILearningPath for edit and NewLearningPathFormGroupInput for create.
 */
type LearningPathFormGroupInput = ILearningPath | PartialWithRequiredKeyOf<NewLearningPath>;

type LearningPathFormDefaults = Pick<NewLearningPath, 'id'>;

type LearningPathFormGroupContent = {
  id: FormControl<ILearningPath['id'] | NewLearningPath['id']>;
  examId: FormControl<ILearningPath['examId']>;
  subcontentId: FormControl<ILearningPath['subcontentId']>;
  order: FormControl<ILearningPath['order']>;
  exam: FormControl<ILearningPath['exam']>;
  subcontent: FormControl<ILearningPath['subcontent']>;
};

export type LearningPathFormGroup = FormGroup<LearningPathFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LearningPathFormService {
  createLearningPathFormGroup(learningPath: LearningPathFormGroupInput = { id: null }): LearningPathFormGroup {
    const learningPathRawValue = {
      ...this.getFormDefaults(),
      ...learningPath,
    };
    return new FormGroup<LearningPathFormGroupContent>({
      id: new FormControl(
        { value: learningPathRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      examId: new FormControl(learningPathRawValue.examId),
      subcontentId: new FormControl(learningPathRawValue.subcontentId),
      order: new FormControl(learningPathRawValue.order),
      exam: new FormControl(learningPathRawValue.exam),
      subcontent: new FormControl(learningPathRawValue.subcontent),
    });
  }

  getLearningPath(form: LearningPathFormGroup): ILearningPath | NewLearningPath {
    return form.getRawValue() as ILearningPath | NewLearningPath;
  }

  resetForm(form: LearningPathFormGroup, learningPath: LearningPathFormGroupInput): void {
    const learningPathRawValue = { ...this.getFormDefaults(), ...learningPath };
    form.reset(
      {
        ...learningPathRawValue,
        id: { value: learningPathRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LearningPathFormDefaults {
    return {
      id: null,
    };
  }
}
