import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrandModelPage } from './brand-model-page';

describe('BrandModelPage', () => {
  let component: BrandModelPage;
  let fixture: ComponentFixture<BrandModelPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BrandModelPage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BrandModelPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
