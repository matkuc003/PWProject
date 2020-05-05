import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {User} from "../model/user";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private USER_URL = environment.mainURL + "/api/user";
  private SAVE_USER = this.USER_URL;

  constructor(private http: HttpClient) { }

  saveUser(user: User): Observable<User> {
    return this.http.post<User>(this.SAVE_USER, user);
  }
}
