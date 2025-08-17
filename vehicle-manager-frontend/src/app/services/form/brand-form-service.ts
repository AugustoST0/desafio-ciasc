import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';
import { Brand } from '../../interfaces/Brand';

@Injectable({
  providedIn: 'root',
})
export class BrandFormService {
  private isVisibleSubject = new BehaviorSubject<boolean>(false);
  private brandDataSubject = new BehaviorSubject<Brand | null>(null);
  private brandInsertedSubject = new Subject<Brand>();
  private brandUpdatedSubject = new Subject<Brand>();

  isVisible$ = this.isVisibleSubject.asObservable();
  brandData$ = this.brandDataSubject.asObservable();
  brandInserted$ = this.brandInsertedSubject.asObservable();
  brandUpdated$ = this.brandUpdatedSubject.asObservable();

  open(brand?: Brand) {
    if (brand) {
      this.brandDataSubject.next(brand); // editando
    } else {
      this.brandDataSubject.next(null); // criando
    }
    this.isVisibleSubject.next(true);
  }

  close() {
    this.isVisibleSubject.next(false);
  }
}
