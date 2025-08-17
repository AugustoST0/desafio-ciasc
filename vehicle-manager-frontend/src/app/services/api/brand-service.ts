import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Brand } from '../../interfaces/Brand';

@Injectable({
  providedIn: 'root',
})
export class BrandService {
  baseApiUrl = environment.baseApiUrl;
  apiUrl = `${this.baseApiUrl}/brands`;

  private brandsSubject = new BehaviorSubject<Brand[]>([]);
  brands$ = this.brandsSubject.asObservable();

  constructor(private http: HttpClient) {}

  getAll(): Observable<Brand[]> {
    return this.http
      .get<Brand[]>(this.apiUrl)
      .pipe(tap((brands) => this.brandsSubject.next(brands)));
  }

  getById(id: number): Observable<Brand> {
    return this.http.get<Brand>(`${this.apiUrl}/${id}`);
  }

  insert(brand: Brand): Observable<Brand> {
    return this.http.post<Brand>(`${this.apiUrl}`, brand).pipe(
      tap((newBrand) => {
        const current = this.brandsSubject.getValue();
        this.brandsSubject.next([...current, newBrand]);
      })
    );
  }

  update(id: number, brand: Brand): Observable<Brand> {
    return this.http.put<Brand>(`${this.apiUrl}/${id}`, brand).pipe(
      tap((updatedBrand) => {
        const current = this.brandsSubject.getValue();
        const index = current.findIndex((b) => b.id === id);
        current[index] = updatedBrand;
        this.brandsSubject.next([...current]);
      })
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(
      tap(() => {
        const current = this.brandsSubject.getValue();
        this.brandsSubject.next(current.filter((b) => b.id !== id));
      })
    );
  }
}
