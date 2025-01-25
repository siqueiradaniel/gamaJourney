import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ContentService } from '../service/content.service';
import { IContent } from '../content.model';
import { ContentFormService } from './content-form.service';

import { ContentUpdateComponent } from './content-update.component';

describe('Content Management Update Component', () => {
  let comp: ContentUpdateComponent;
  let fixture: ComponentFixture<ContentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contentFormService: ContentFormService;
  let contentService: ContentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ContentUpdateComponent],
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
      .overrideTemplate(ContentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contentFormService = TestBed.inject(ContentFormService);
    contentService = TestBed.inject(ContentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const content: IContent = { id: '4dd1e232-4125-4f33-9d38-5c4e65096fc6' };

      activatedRoute.data = of({ content });
      comp.ngOnInit();

      expect(comp.content).toEqual(content);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContent>>();
      const content = { id: '2c45dd30-cc68-4449-8234-3960f9956c9b' };
      jest.spyOn(contentFormService, 'getContent').mockReturnValue(content);
      jest.spyOn(contentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ content });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: content }));
      saveSubject.complete();

      // THEN
      expect(contentFormService.getContent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contentService.update).toHaveBeenCalledWith(expect.objectContaining(content));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContent>>();
      const content = { id: '2c45dd30-cc68-4449-8234-3960f9956c9b' };
      jest.spyOn(contentFormService, 'getContent').mockReturnValue({ id: null });
      jest.spyOn(contentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ content: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: content }));
      saveSubject.complete();

      // THEN
      expect(contentFormService.getContent).toHaveBeenCalled();
      expect(contentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContent>>();
      const content = { id: '2c45dd30-cc68-4449-8234-3960f9956c9b' };
      jest.spyOn(contentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ content });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
