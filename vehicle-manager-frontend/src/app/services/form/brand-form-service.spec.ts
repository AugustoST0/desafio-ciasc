import { TestBed } from '@angular/core/testing';

import { BrandFormService } from './brand-form-service';

describe('BrandFormService', () => {
  let service: BrandFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BrandFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
