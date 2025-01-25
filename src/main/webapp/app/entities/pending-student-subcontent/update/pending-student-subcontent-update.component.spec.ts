import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISubcontent } from 'app/entities/subcontent/subcontent.model';
import { SubcontentService } from 'app/entities/subcontent/service/subcontent.service';
import { IStudent } from 'app/entities/student/student.model';
import { StudentService } from 'app/entities/student/service/student.service';
import { IPendingStudentSubcontent } from '../pending-student-subcontent.model';
import { PendingStudentSubcontentService } from '../service/pending-student-subcontent.service';
import { PendingStudentSubcontentFormService } from './pending-student-subcontent-form.service';

import { PendingStudentSubcontentUpdateComponent } from './pending-student-subcontent-update.component';

describe('PendingStudentSubcontent Management Update Component', () => {
  let comp: PendingStudentSubcontentUpdateComponent;
  let fixture: ComponentFixture<PendingStudentSubcontentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pendingStudentSubcontentFormService: PendingStudentSubcontentFormService;
  let pendingStudentSubcontentService: PendingStudentSubcontentService;
  let subcontentService: SubcontentService;
  let studentService: StudentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PendingStudentSubcontentUpdateComponent],
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
      .overrideTemplate(PendingStudentSubcontentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PendingStudentSubcontentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pendingStudentSubcontentFormService = TestBed.inject(PendingStudentSubcontentFormService);
    pendingStudentSubcontentService = TestBed.inject(PendingStudentSubcontentService);
    subcontentService = TestBed.inject(SubcontentService);
    studentService = TestBed.inject(StudentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Subcontent query and add missing value', () => {
      const pendingStudentSubcontent: IPendingStudentSubcontent = { id: 1204 };
      const subcontent: ISubcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      pendingStudentSubcontent.subcontent = subcontent;

      const subcontentCollection: ISubcontent[] = [{ id: 'a458ef71-ece5-459b-a91f-8893254df8f1' }];
      jest.spyOn(subcontentService, 'query').mockReturnValue(of(new HttpResponse({ body: subcontentCollection })));
      const additionalSubcontents = [subcontent];
      const expectedCollection: ISubcontent[] = [...additionalSubcontents, ...subcontentCollection];
      jest.spyOn(subcontentService, 'addSubcontentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pendingStudentSubcontent });
      comp.ngOnInit();

      expect(subcontentService.query).toHaveBeenCalled();
      expect(subcontentService.addSubcontentToCollectionIfMissing).toHaveBeenCalledWith(
        subcontentCollection,
        ...additionalSubcontents.map(expect.objectContaining),
      );
      expect(comp.subcontentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Student query and add missing value', () => {
      const pendingStudentSubcontent: IPendingStudentSubcontent = { id: 1204 };
      const student: IStudent = { id: '4b4bd123-76cd-4436-82e0-2256af75949b' };
      pendingStudentSubcontent.student = student;

      const studentCollection: IStudent[] = [{ id: '4b4bd123-76cd-4436-82e0-2256af75949b' }];
      jest.spyOn(studentService, 'query').mockReturnValue(of(new HttpResponse({ body: studentCollection })));
      const additionalStudents = [student];
      const expectedCollection: IStudent[] = [...additionalStudents, ...studentCollection];
      jest.spyOn(studentService, 'addStudentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ pendingStudentSubcontent });
      comp.ngOnInit();

      expect(studentService.query).toHaveBeenCalled();
      expect(studentService.addStudentToCollectionIfMissing).toHaveBeenCalledWith(
        studentCollection,
        ...additionalStudents.map(expect.objectContaining),
      );
      expect(comp.studentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const pendingStudentSubcontent: IPendingStudentSubcontent = { id: 1204 };
      const subcontent: ISubcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      pendingStudentSubcontent.subcontent = subcontent;
      const student: IStudent = { id: '4b4bd123-76cd-4436-82e0-2256af75949b' };
      pendingStudentSubcontent.student = student;

      activatedRoute.data = of({ pendingStudentSubcontent });
      comp.ngOnInit();

      expect(comp.subcontentsSharedCollection).toContainEqual(subcontent);
      expect(comp.studentsSharedCollection).toContainEqual(student);
      expect(comp.pendingStudentSubcontent).toEqual(pendingStudentSubcontent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPendingStudentSubcontent>>();
      const pendingStudentSubcontent = { id: 29412 };
      jest.spyOn(pendingStudentSubcontentFormService, 'getPendingStudentSubcontent').mockReturnValue(pendingStudentSubcontent);
      jest.spyOn(pendingStudentSubcontentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pendingStudentSubcontent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pendingStudentSubcontent }));
      saveSubject.complete();

      // THEN
      expect(pendingStudentSubcontentFormService.getPendingStudentSubcontent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pendingStudentSubcontentService.update).toHaveBeenCalledWith(expect.objectContaining(pendingStudentSubcontent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPendingStudentSubcontent>>();
      const pendingStudentSubcontent = { id: 29412 };
      jest.spyOn(pendingStudentSubcontentFormService, 'getPendingStudentSubcontent').mockReturnValue({ id: null });
      jest.spyOn(pendingStudentSubcontentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pendingStudentSubcontent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pendingStudentSubcontent }));
      saveSubject.complete();

      // THEN
      expect(pendingStudentSubcontentFormService.getPendingStudentSubcontent).toHaveBeenCalled();
      expect(pendingStudentSubcontentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPendingStudentSubcontent>>();
      const pendingStudentSubcontent = { id: 29412 };
      jest.spyOn(pendingStudentSubcontentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pendingStudentSubcontent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pendingStudentSubcontentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSubcontent', () => {
      it('Should forward to subcontentService', () => {
        const entity = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
        const entity2 = { id: 'ccf35440-d8be-425d-b236-5a95d83f4e8c' };
        jest.spyOn(subcontentService, 'compareSubcontent');
        comp.compareSubcontent(entity, entity2);
        expect(subcontentService.compareSubcontent).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStudent', () => {
      it('Should forward to studentService', () => {
        const entity = { id: '4b4bd123-76cd-4436-82e0-2256af75949b' };
        const entity2 = { id: '7367340f-f03a-4f57-be07-9afaafff3e9c' };
        jest.spyOn(studentService, 'compareStudent');
        comp.compareStudent(entity, entity2);
        expect(studentService.compareStudent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
