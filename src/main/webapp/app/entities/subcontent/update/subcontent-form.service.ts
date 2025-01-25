import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISubcontent, NewSubcontent } from '../subcontent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubcontent for edit and NewSubcontentFormGroupInput for create.
 */
type SubcontentFormGroupInput = ISubcontent | PartialWithRequiredKeyOf<NewSubcontent>;

type SubcontentFormDefaults = Pick<NewSubcontent, 'id'>;

type SubcontentFormGroupContent = {
  id: FormControl<ISubcontent['id'] | NewSubcontent['id']>;
  name: FormControl<ISubcontent['name']>;
  description: FormControl<ISubcontent['description']>;
  content: FormControl<ISubcontent['content']>;
};

export type SubcontentFormGroup = FormGroup<SubcontentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubcontentFormService {
  createSubcontentFormGroup(subcontent: SubcontentFormGroupInput = { id: null }): SubcontentFormGroup {
    const subcontentRawValue = {
      ...this.getFormDefaults(),
      ...subcontent,
    };
    return new FormGroup<SubcontentFormGroupContent>({
      id: new FormControl(
        { value: subcontentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(subcontentRawValue.name),
      description: new FormControl(subcontentRawValue.description),
      content: new FormControl(subcontentRawValue.content),
    });
  }

  getSubcontent(form: SubcontentFormGroup): ISubcontent | NewSubcontent {
    return form.getRawValue() as ISubcontent | NewSubcontent;
  }

  resetForm(form: SubcontentFormGroup, subcontent: SubcontentFormGroupInput): void {
    const subcontentRawValue = { ...this.getFormDefaults(), ...subcontent };
    form.reset(
      {
        ...subcontentRawValue,
        id: { value: subcontentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubcontentFormDefaults {
    return {
      id: null,
    };
  }
}
