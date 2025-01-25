import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../subcontent.test-samples';

import { SubcontentFormService } from './subcontent-form.service';

describe('Subcontent Form Service', () => {
  let service: SubcontentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubcontentFormService);
  });

  describe('Service methods', () => {
    describe('createSubcontentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubcontentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            content: expect.any(Object),
          }),
        );
      });

      it('passing ISubcontent should create a new form with FormGroup', () => {
        const formGroup = service.createSubcontentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            content: expect.any(Object),
          }),
        );
      });
    });

    describe('getSubcontent', () => {
      it('should return NewSubcontent for default Subcontent initial value', () => {
        const formGroup = service.createSubcontentFormGroup(sampleWithNewData);

        const subcontent = service.getSubcontent(formGroup) as any;

        expect(subcontent).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubcontent for empty Subcontent initial value', () => {
        const formGroup = service.createSubcontentFormGroup();

        const subcontent = service.getSubcontent(formGroup) as any;

        expect(subcontent).toMatchObject({});
      });

      it('should return ISubcontent', () => {
        const formGroup = service.createSubcontentFormGroup(sampleWithRequiredData);

        const subcontent = service.getSubcontent(formGroup) as any;

        expect(subcontent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubcontent should not enable id FormControl', () => {
        const formGroup = service.createSubcontentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubcontent should disable id FormControl', () => {
        const formGroup = service.createSubcontentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
