import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

import { User } from '../interfaces/User';

import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  baseApiUrl = environment.baseApiUrl;
  apiUrl = `${this.baseApiUrl}/users`;

  constructor(private http: HttpClient) {}

  registerUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, user);
  }
}
