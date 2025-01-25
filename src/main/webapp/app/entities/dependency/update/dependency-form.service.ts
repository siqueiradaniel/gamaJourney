import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDependency, NewDependency } from '../dependency.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDependency for edit and NewDependencyFormGroupInput for create.
 */
type DependencyFormGroupInput = IDependency | PartialWithRequiredKeyOf<NewDependency>;

type DependencyFormDefaults = Pick<NewDependency, 'id'>;

type DependencyFormGroupContent = {
  id: FormControl<IDependency['id'] | NewDependency['id']>;
  firstSubcontentId: FormControl<IDependency['firstSubcontentId']>;
  secondSubcontentId: FormControl<IDependency['secondSubcontentId']>;
  subcontent: FormControl<IDependency['subcontent']>;
};

export type DependencyFormGroup = FormGroup<DependencyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DependencyFormService {
  createDependencyFormGroup(dependency: DependencyFormGroupInput = { id: null }): DependencyFormGroup {
    const dependencyRawValue = {
      ...this.getFormDefaults(),
      ...dependency,
    };
    return new FormGroup<DependencyFormGroupContent>({
      id: new FormControl(
        { value: dependencyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstSubcontentId: new FormControl(dependencyRawValue.firstSubcontentId),
      secondSubcontentId: new FormControl(dependencyRawValue.secondSubcontentId),
      subcontent: new FormControl(dependencyRawValue.subcontent),
    });
  }

  getDependency(form: DependencyFormGroup): IDependency | NewDependency {
    return form.getRawValue() as IDependency | NewDependency;
  }

  resetForm(form: DependencyFormGroup, dependency: DependencyFormGroupInput): void {
    const dependencyRawValue = { ...this.getFormDefaults(), ...dependency };
    form.reset(
      {
        ...dependencyRawValue,
        id: { value: dependencyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DependencyFormDefaults {
    return {
      id: null,
    };
  }
}
