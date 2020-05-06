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
  private SAVE_USER = this.USER_URL+"/create";
  private checkURL = this.USER_URL + "/check";
  private deleteURL = this.USER_URL + "/delete";

  constructor(private http: HttpClient) { }

  saveUser(user: User): Observable<User> {
    return this.http.post<User>(this.SAVE_USER, user);
  }
  checkUser(user:User): Observable<boolean>{

    return this.http.post<boolean>(this.checkURL,user);
  }
  deleteUser(user: User): Observable<boolean> {
    return this.http.post<boolean>(this.deleteURL, user);
  }
}
