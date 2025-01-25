import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../learning-path.test-samples';

import { LearningPathFormService } from './learning-path-form.service';

describe('LearningPath Form Service', () => {
  let service: LearningPathFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LearningPathFormService);
  });

  describe('Service methods', () => {
    describe('createLearningPathFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createLearningPathFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            examId: expect.any(Object),
            subcontentId: expect.any(Object),
            order: expect.any(Object),
            exam: expect.any(Object),
            subcontent: expect.any(Object),
          }),
        );
      });

      it('passing ILearningPath should create a new form with FormGroup', () => {
        const formGroup = service.createLearningPathFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            examId: expect.any(Object),
            subcontentId: expect.any(Object),
            order: expect.any(Object),
            exam: expect.any(Object),
            subcontent: expect.any(Object),
          }),
        );
      });
    });

    describe('getLearningPath', () => {
      it('should return NewLearningPath for default LearningPath initial value', () => {
        const formGroup = service.createLearningPathFormGroup(sampleWithNewData);

        const learningPath = service.getLearningPath(formGroup) as any;

        expect(learningPath).toMatchObject(sampleWithNewData);
      });

      it('should return NewLearningPath for empty LearningPath initial value', () => {
        const formGroup = service.createLearningPathFormGroup();

        const learningPath = service.getLearningPath(formGroup) as any;

        expect(learningPath).toMatchObject({});
      });

      it('should return ILearningPath', () => {
        const formGroup = service.createLearningPathFormGroup(sampleWithRequiredData);

        const learningPath = service.getLearningPath(formGroup) as any;

        expect(learningPath).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ILearningPath should not enable id FormControl', () => {
        const formGroup = service.createLearningPathFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewLearningPath should disable id FormControl', () => {
        const formGroup = service.createLearningPathFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
