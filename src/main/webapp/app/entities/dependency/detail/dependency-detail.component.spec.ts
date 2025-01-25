import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DependencyDetailComponent } from './dependency-detail.component';

describe('Dependency Management Detail Component', () => {
  let comp: DependencyDetailComponent;
  let fixture: ComponentFixture<DependencyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DependencyDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./dependency-detail.component').then(m => m.DependencyDetailComponent),
              resolve: { dependency: () => of({ id: 6675 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DependencyDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DependencyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dependency on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DependencyDetailComponent);

      // THEN
      expect(instance.dependency()).toEqual(expect.objectContaining({ id: 6675 }));
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
