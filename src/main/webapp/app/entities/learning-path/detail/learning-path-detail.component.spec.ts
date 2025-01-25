import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { LearningPathDetailComponent } from './learning-path-detail.component';

describe('LearningPath Management Detail Component', () => {
  let comp: LearningPathDetailComponent;
  let fixture: ComponentFixture<LearningPathDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LearningPathDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./learning-path-detail.component').then(m => m.LearningPathDetailComponent),
              resolve: { learningPath: () => of({ id: 6936 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LearningPathDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LearningPathDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load learningPath on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LearningPathDetailComponent);

      // THEN
      expect(instance.learningPath()).toEqual(expect.objectContaining({ id: 6936 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
