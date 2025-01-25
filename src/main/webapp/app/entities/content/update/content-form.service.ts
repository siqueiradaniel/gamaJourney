import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContent, NewContent } from '../content.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContent for edit and NewContentFormGroupInput for create.
 */
type ContentFormGroupInput = IContent | PartialWithRequiredKeyOf<NewContent>;

type ContentFormDefaults = Pick<NewContent, 'id'>;

type ContentFormGroupContent = {
  id: FormControl<IContent['id'] | NewContent['id']>;
  name: FormControl<IContent['name']>;
  description: FormControl<IContent['description']>;
};

export type ContentFormGroup = FormGroup<ContentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ContentFormService {
  createContentFormGroup(content: ContentFormGroupInput = { id: null }): ContentFormGroup {
    const contentRawValue = {
      ...this.getFormDefaults(),
      ...content,
    };
    return new FormGroup<ContentFormGroupContent>({
      id: new FormControl(
        { value: contentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(contentRawValue.name),
      description: new FormControl(contentRawValue.description),
    });
  }

  getContent(form: ContentFormGroup): IContent | NewContent {
    return form.getRawValue() as IContent | NewContent;
  }

  resetForm(form: ContentFormGroup, content: ContentFormGroupInput): void {
    const contentRawValue = { ...this.getFormDefaults(), ...content };
    form.reset(
      {
        ...contentRawValue,
        id: { value: contentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ContentFormDefaults {
    return {
      id: null,
    };
  }
}
