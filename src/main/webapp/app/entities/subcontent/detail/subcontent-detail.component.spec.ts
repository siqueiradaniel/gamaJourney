import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SubcontentDetailComponent } from './subcontent-detail.component';

describe('Subcontent Management Detail Component', () => {
  let comp: SubcontentDetailComponent;
  let fixture: ComponentFixture<SubcontentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubcontentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./subcontent-detail.component').then(m => m.SubcontentDetailComponent),
              resolve: { subcontent: () => of({ id: 'a458ef71-ece5-459b-a91f-8893254df8f1' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SubcontentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubcontentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load subcontent on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SubcontentDetailComponent);

      // THEN
      expect(instance.subcontent()).toEqual(expect.objectContaining({ id: 'a458ef71-ece5-459b-a91f-8893254df8f1' }));
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
