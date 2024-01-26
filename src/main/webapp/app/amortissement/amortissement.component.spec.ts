import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AmortissementComponent } from './amortissement.component';

describe('AmortissementComponent', () => {
  let component: AmortissementComponent;
  let fixture: ComponentFixture<AmortissementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmortissementComponent],
    });
    fixture = TestBed.createComponent(AmortissementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
