import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IExam } from 'app/entities/exam/exam.model';
import { ExamService } from 'app/entities/exam/service/exam.service';
import { ISubcontent } from 'app/entities/subcontent/subcontent.model';
import { SubcontentService } from 'app/entities/subcontent/service/subcontent.service';
import { ILearningPath } from '../learning-path.model';
import { LearningPathService } from '../service/learning-path.service';
import { LearningPathFormService } from './learning-path-form.service';

import { LearningPathUpdateComponent } from './learning-path-update.component';

describe('LearningPath Management Update Component', () => {
  let comp: LearningPathUpdateComponent;
  let fixture: ComponentFixture<LearningPathUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let learningPathFormService: LearningPathFormService;
  let learningPathService: LearningPathService;
  let examService: ExamService;
  let subcontentService: SubcontentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LearningPathUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LearningPathUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LearningPathUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    learningPathFormService = TestBed.inject(LearningPathFormService);
    learningPathService = TestBed.inject(LearningPathService);
    examService = TestBed.inject(ExamService);
    subcontentService = TestBed.inject(SubcontentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Exam query and add missing value', () => {
      const learningPath: ILearningPath = { id: 25742 };
      const exam: IExam = { id: '76c575c0-4181-4bc3-8fd5-1f26faadec72' };
      learningPath.exam = exam;

      const examCollection: IExam[] = [{ id: '76c575c0-4181-4bc3-8fd5-1f26faadec72' }];
      jest.spyOn(examService, 'query').mockReturnValue(of(new HttpResponse({ body: examCollection })));
      const additionalExams = [exam];
      const expectedCollection: IExam[] = [...additionalExams, ...examCollection];
      jest.spyOn(examService, 'addExamToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ learningPath });
      comp.ngOnInit();

      expect(examService.query).toHaveBeenCalled();
      expect(examService.addExamToCollectionIfMissing).toHaveBeenCalledWith(
        examCollection,
        ...additionalExams.map(expect.objectContaining),
      );
      expect(comp.examsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Subcontent query and add missing value', () => {
      const learningPath: ILearningPath = { id: 25742 };
      const subcontent: ISubcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      learningPath.subcontent = subcontent;

      const subcontentCollection: ISubcontent[] = [{ id: 'a458ef71-ece5-459b-a91f-8893254df8f1' }];
      jest.spyOn(subcontentService, 'query').mockReturnValue(of(new HttpResponse({ body: subcontentCollection })));
      const additionalSubcontents = [subcontent];
      const expectedCollection: ISubcontent[] = [...additionalSubcontents, ...subcontentCollection];
      jest.spyOn(subcontentService, 'addSubcontentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ learningPath });
      comp.ngOnInit();

      expect(subcontentService.query).toHaveBeenCalled();
      expect(subcontentService.addSubcontentToCollectionIfMissing).toHaveBeenCalledWith(
        subcontentCollection,
        ...additionalSubcontents.map(expect.objectContaining),
      );
      expect(comp.subcontentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const learningPath: ILearningPath = { id: 25742 };
      const exam: IExam = { id: '76c575c0-4181-4bc3-8fd5-1f26faadec72' };
      learningPath.exam = exam;
      const subcontent: ISubcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      learningPath.subcontent = subcontent;

      activatedRoute.data = of({ learningPath });
      comp.ngOnInit();

      expect(comp.examsSharedCollection).toContainEqual(exam);
      expect(comp.subcontentsSharedCollection).toContainEqual(subcontent);
      expect(comp.learningPath).toEqual(learningPath);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILearningPath>>();
      const learningPath = { id: 6936 };
      jest.spyOn(learningPathFormService, 'getLearningPath').mockReturnValue(learningPath);
      jest.spyOn(learningPathService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ learningPath });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: learningPath }));
      saveSubject.complete();

      // THEN
      expect(learningPathFormService.getLearningPath).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(learningPathService.update).toHaveBeenCalledWith(expect.objectContaining(learningPath));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILearningPath>>();
      const learningPath = { id: 6936 };
      jest.spyOn(learningPathFormService, 'getLearningPath').mockReturnValue({ id: null });
      jest.spyOn(learningPathService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ learningPath: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: learningPath }));
      saveSubject.complete();

      // THEN
      expect(learningPathFormService.getLearningPath).toHaveBeenCalled();
      expect(learningPathService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILearningPath>>();
      const learningPath = { id: 6936 };
      jest.spyOn(learningPathService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ learningPath });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(learningPathService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareExam', () => {
      it('Should forward to examService', () => {
        const entity = { id: '76c575c0-4181-4bc3-8fd5-1f26faadec72' };
        const entity2 = { id: '8fd7d5d8-491d-47df-ae8c-fc7fc485a98d' };
        jest.spyOn(examService, 'compareExam');
        comp.compareExam(entity, entity2);
        expect(examService.compareExam).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSubcontent', () => {
      it('Should forward to subcontentService', () => {
        const entity = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
        const entity2 = { id: 'ccf35440-d8be-425d-b236-5a95d83f4e8c' };
        jest.spyOn(subcontentService, 'compareSubcontent');
        comp.compareSubcontent(entity, entity2);
        expect(subcontentService.compareSubcontent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
