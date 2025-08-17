import { Injectable } from '@angular/core';
import { Subject, BehaviorSubject } from 'rxjs';
import { Model } from '../../interfaces/Model';

@Injectable({
  providedIn: 'root',
})
export class ModelFormService {
  private isVisibleSubject = new BehaviorSubject<boolean>(false);
  private modelDataSubject = new BehaviorSubject<Model | null>(null);
  private modelInsertedSubject = new Subject<Model>();
  private modelUpdatedSubject = new Subject<Model>();

  isVisible$ = this.isVisibleSubject.asObservable();
  modelData$ = this.modelDataSubject.asObservable();
  modelInserted$ = this.modelInsertedSubject.asObservable();
  modelUpdated$ = this.modelUpdatedSubject.asObservable();

  open(model?: Model) {
    if (model) {
      this.modelDataSubject.next(model); // editando
    } else {
      this.modelDataSubject.next(null); // criando
    }
    this.isVisibleSubject.next(true);
  }

  close() {
    this.isVisibleSubject.next(false);
  }
}
