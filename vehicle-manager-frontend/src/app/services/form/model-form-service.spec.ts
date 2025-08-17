import { TestBed } from '@angular/core/testing';

import { ModelFormService } from './model-form-service';

describe('ModelFormService', () => {
  let service: ModelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModelFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
