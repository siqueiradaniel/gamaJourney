import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IContent } from 'app/entities/content/content.model';
import { ContentService } from 'app/entities/content/service/content.service';
import { SubcontentService } from '../service/subcontent.service';
import { ISubcontent } from '../subcontent.model';
import { SubcontentFormService } from './subcontent-form.service';

import { SubcontentUpdateComponent } from './subcontent-update.component';

describe('Subcontent Management Update Component', () => {
  let comp: SubcontentUpdateComponent;
  let fixture: ComponentFixture<SubcontentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subcontentFormService: SubcontentFormService;
  let subcontentService: SubcontentService;
  let contentService: ContentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SubcontentUpdateComponent],
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
      .overrideTemplate(SubcontentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubcontentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subcontentFormService = TestBed.inject(SubcontentFormService);
    subcontentService = TestBed.inject(SubcontentService);
    contentService = TestBed.inject(ContentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Content query and add missing value', () => {
      const subcontent: ISubcontent = { id: 'ccf35440-d8be-425d-b236-5a95d83f4e8c' };
      const content: IContent = { id: '2c45dd30-cc68-4449-8234-3960f9956c9b' };
      subcontent.content = content;

      const contentCollection: IContent[] = [{ id: '2c45dd30-cc68-4449-8234-3960f9956c9b' }];
      jest.spyOn(contentService, 'query').mockReturnValue(of(new HttpResponse({ body: contentCollection })));
      const additionalContents = [content];
      const expectedCollection: IContent[] = [...additionalContents, ...contentCollection];
      jest.spyOn(contentService, 'addContentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ subcontent });
      comp.ngOnInit();

      expect(contentService.query).toHaveBeenCalled();
      expect(contentService.addContentToCollectionIfMissing).toHaveBeenCalledWith(
        contentCollection,
        ...additionalContents.map(expect.objectContaining),
      );
      expect(comp.contentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const subcontent: ISubcontent = { id: 'ccf35440-d8be-425d-b236-5a95d83f4e8c' };
      const content: IContent = { id: '2c45dd30-cc68-4449-8234-3960f9956c9b' };
      subcontent.content = content;

      activatedRoute.data = of({ subcontent });
      comp.ngOnInit();

      expect(comp.contentsSharedCollection).toContainEqual(content);
      expect(comp.subcontent).toEqual(subcontent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubcontent>>();
      const subcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      jest.spyOn(subcontentFormService, 'getSubcontent').mockReturnValue(subcontent);
      jest.spyOn(subcontentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subcontent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subcontent }));
      saveSubject.complete();

      // THEN
      expect(subcontentFormService.getSubcontent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(subcontentService.update).toHaveBeenCalledWith(expect.objectContaining(subcontent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubcontent>>();
      const subcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      jest.spyOn(subcontentFormService, 'getSubcontent').mockReturnValue({ id: null });
      jest.spyOn(subcontentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subcontent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subcontent }));
      saveSubject.complete();

      // THEN
      expect(subcontentFormService.getSubcontent).toHaveBeenCalled();
      expect(subcontentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubcontent>>();
      const subcontent = { id: 'a458ef71-ece5-459b-a91f-8893254df8f1' };
      jest.spyOn(subcontentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subcontent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subcontentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareContent', () => {
      it('Should forward to contentService', () => {
        const entity = { id: '2c45dd30-cc68-4449-8234-3960f9956c9b' };
        const entity2 = { id: '4dd1e232-4125-4f33-9d38-5c4e65096fc6' };
        jest.spyOn(contentService, 'compareContent');
        comp.compareContent(entity, entity2);
        expect(contentService.compareContent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
