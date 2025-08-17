import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Model } from '../../interfaces/Model';

@Injectable({
  providedIn: 'root',
})
export class ModelService {
  baseApiUrl = environment.baseApiUrl;
  apiUrl = `${this.baseApiUrl}/modelos`;

  private modelsSubject = new BehaviorSubject<Model[]>([]);
  models$ = this.modelsSubject.asObservable();

  constructor(private http: HttpClient) {}

  getAll(): Observable<Model[]> {
    return this.http
      .get<Model[]>(this.apiUrl)
      .pipe(tap((models) => this.modelsSubject.next(models)));
  }

  getById(id: number): Observable<Model> {
    return this.http.get<Model>(`${this.apiUrl}/${id}`);
  }

  insert(model: Model): Observable<Model> {
    return this.http.post<Model>(`${this.apiUrl}`, model).pipe(
      tap((newModel) => {
        const current = this.modelsSubject.getValue();
        this.modelsSubject.next([...current, newModel]);
      })
    );
  }

  update(id: number, model: Model): Observable<Model> {
    return this.http.put<Model>(`${this.apiUrl}/${id}`, model).pipe(
      tap((updatedModel) => {
        const current = this.modelsSubject.getValue();
        const index = current.findIndex((m) => m.id === id);
        current[index] = updatedModel;
        this.modelsSubject.next([...current]);
      })
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => {
        const current = this.modelsSubject.getValue();
        this.modelsSubject.next(current.filter((m) => m.id !== id));
      })
    );
  }
}
