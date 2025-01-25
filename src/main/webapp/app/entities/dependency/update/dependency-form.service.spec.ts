import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../dependency.test-samples';

import { DependencyFormService } from './dependency-form.service';

describe('Dependency Form Service', () => {
  let service: DependencyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DependencyFormService);
  });

  describe('Service methods', () => {
    describe('createDependencyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDependencyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstSubcontentId: expect.any(Object),
            secondSubcontentId: expect.any(Object),
            subcontent: expect.any(Object),
          }),
        );
      });

      it('passing IDependency should create a new form with FormGroup', () => {
        const formGroup = service.createDependencyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstSubcontentId: expect.any(Object),
            secondSubcontentId: expect.any(Object),
            subcontent: expect.any(Object),
          }),
        );
      });
    });

    describe('getDependency', () => {
      it('should return NewDependency for default Dependency initial value', () => {
        const formGroup = service.createDependencyFormGroup(sampleWithNewData);

        const dependency = service.getDependency(formGroup) as any;

        expect(dependency).toMatchObject(sampleWithNewData);
      });

      it('should return NewDependency for empty Dependency initial value', () => {
        const formGroup = service.createDependencyFormGroup();

        const dependency = service.getDependency(formGroup) as any;

        expect(dependency).toMatchObject({});
      });

      it('should return IDependency', () => {
        const formGroup = service.createDependencyFormGroup(sampleWithRequiredData);

        const dependency = service.getDependency(formGroup) as any;

        expect(dependency).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDependency should not enable id FormControl', () => {
        const formGroup = service.createDependencyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDependency should disable id FormControl', () => {
        const formGroup = service.createDependencyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
