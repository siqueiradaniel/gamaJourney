import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../pending-student-subcontent.test-samples';

import { PendingStudentSubcontentFormService } from './pending-student-subcontent-form.service';

describe('PendingStudentSubcontent Form Service', () => {
  let service: PendingStudentSubcontentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PendingStudentSubcontentFormService);
  });

  describe('Service methods', () => {
    describe('createPendingStudentSubcontentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPendingStudentSubcontentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            studentId: expect.any(Object),
            subcontentId: expect.any(Object),
            currentStatus: expect.any(Object),
            subcontent: expect.any(Object),
            student: expect.any(Object),
          }),
        );
      });

      it('passing IPendingStudentSubcontent should create a new form with FormGroup', () => {
        const formGroup = service.createPendingStudentSubcontentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            studentId: expect.any(Object),
            subcontentId: expect.any(Object),
            currentStatus: expect.any(Object),
            subcontent: expect.any(Object),
            student: expect.any(Object),
          }),
        );
      });
    });

    describe('getPendingStudentSubcontent', () => {
      it('should return NewPendingStudentSubcontent for default PendingStudentSubcontent initial value', () => {
        const formGroup = service.createPendingStudentSubcontentFormGroup(sampleWithNewData);

        const pendingStudentSubcontent = service.getPendingStudentSubcontent(formGroup) as any;

        expect(pendingStudentSubcontent).toMatchObject(sampleWithNewData);
      });

      it('should return NewPendingStudentSubcontent for empty PendingStudentSubcontent initial value', () => {
        const formGroup = service.createPendingStudentSubcontentFormGroup();

        const pendingStudentSubcontent = service.getPendingStudentSubcontent(formGroup) as any;

        expect(pendingStudentSubcontent).toMatchObject({});
      });

      it('should return IPendingStudentSubcontent', () => {
        const formGroup = service.createPendingStudentSubcontentFormGroup(sampleWithRequiredData);

        const pendingStudentSubcontent = service.getPendingStudentSubcontent(formGroup) as any;

        expect(pendingStudentSubcontent).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPendingStudentSubcontent should not enable id FormControl', () => {
        const formGroup = service.createPendingStudentSubcontentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPendingStudentSubcontent should disable id FormControl', () => {
        const formGroup = service.createPendingStudentSubcontentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
