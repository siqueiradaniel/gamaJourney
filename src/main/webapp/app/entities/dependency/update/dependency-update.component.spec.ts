import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISubcontent } from 'app/entities/subcontent/subcontent.model';
import { SubcontentService } from 'app/entities/subcontent/service/subcontent.service';
import { DependencyService } from '../service/dependency.service';
import { IDependency } from '../dependency.model';
import { DependencyFormService } from './dependency-form.service';

import { DependencyUpdateComponent } from './dependency-update.component';

describe('Dependency Management Update Component', () => {
  let comp: DependencyUpdateComponent;
  let fixture: ComponentFixture<DependencyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dependencyFormService: DependencyFormService;
  let dependencyService: DependencyService;
  let subcontentService: SubcontentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DependencyUpdateComponent],
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
      .overrideTemplate(DependencyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DependencyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dependencyFormService = TestBed.inject(DependencyFormService);
    dependencyService = TestBed.inject(DependencyService);
    subcontentService = TestBed.inject(SubcontentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Subcontent query and add missing value', () => {
      const dependency: IDependency = { id: 8386 };
      const subcontent: ISubcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      dependency.subcontent = subcontent;

      const subcontentCollection: ISubcontent[] = [{ id: 'a458ef71-ece5-459b-a91f-8893254df8f1' }];
      jest.spyOn(subcontentService, 'query').mockReturnValue(of(new HttpResponse({ body: subcontentCollection })));
      const additionalSubcontents = [subcontent];
      const expectedCollection: ISubcontent[] = [...additionalSubcontents, ...subcontentCollection];
      jest.spyOn(subcontentService, 'addSubcontentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dependency });
      comp.ngOnInit();

      expect(subcontentService.query).toHaveBeenCalled();
      expect(subcontentService.addSubcontentToCollectionIfMissing).toHaveBeenCalledWith(
        subcontentCollection,
        ...additionalSubcontents.map(expect.objectContaining),
      );
      expect(comp.subcontentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dependency: IDependency = { id: 8386 };
      const subcontent: ISubcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      dependency.subcontent = subcontent;

      activatedRoute.data = of({ dependency });
      comp.ngOnInit();

      expect(comp.subcontentsSharedCollection).toContainEqual(subcontent);
      expect(comp.dependency).toEqual(dependency);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDependency>>();
      const dependency = { id: 6675 };
      jest.spyOn(dependencyFormService, 'getDependency').mockReturnValue(dependency);
      jest.spyOn(dependencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dependency }));
      saveSubject.complete();

      // THEN
      expect(dependencyFormService.getDependency).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dependencyService.update).toHaveBeenCalledWith(expect.objectContaining(dependency));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDependency>>();
      const dependency = { id: 6675 };
      jest.spyOn(dependencyFormService, 'getDependency').mockReturnValue({ id: null });
      jest.spyOn(dependencyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependency: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dependency }));
      saveSubject.complete();

      // THEN
      expect(dependencyFormService.getDependency).toHaveBeenCalled();
      expect(dependencyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDependency>>();
      const dependency = { id: 6675 };
      jest.spyOn(dependencyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dependency });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dependencyService.update).toHaveBeenCalled();
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
  });
});
