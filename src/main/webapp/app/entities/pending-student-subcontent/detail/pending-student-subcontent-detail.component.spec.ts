import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PendingStudentSubcontentDetailComponent } from './pending-student-subcontent-detail.component';

describe('PendingStudentSubcontent Management Detail Component', () => {
  let comp: PendingStudentSubcontentDetailComponent;
  let fixture: ComponentFixture<PendingStudentSubcontentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PendingStudentSubcontentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () =>
                import('./pending-student-subcontent-detail.component').then(m => m.PendingStudentSubcontentDetailComponent),
              resolve: { pendingStudentSubcontent: () => of({ id: 29412 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PendingStudentSubcontentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PendingStudentSubcontentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pendingStudentSubcontent on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PendingStudentSubcontentDetailComponent);

      // THEN
      expect(instance.pendingStudentSubcontent()).toEqual(expect.objectContaining({ id: 29412 }));
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
