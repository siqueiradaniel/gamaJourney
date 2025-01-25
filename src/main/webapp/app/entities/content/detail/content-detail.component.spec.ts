import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ContentDetailComponent } from './content-detail.component';

describe('Content Management Detail Component', () => {
  let comp: ContentDetailComponent;
  let fixture: ComponentFixture<ContentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./content-detail.component').then(m => m.ContentDetailComponent),
              resolve: { content: () => of({ id: '2c45dd30-cc68-4449-8234-3960f9956c9b' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ContentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load content on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContentDetailComponent);

      // THEN
      expect(instance.content()).toEqual(expect.objectContaining({ id: '2c45dd30-cc68-4449-8234-3960f9956c9b' }));
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
