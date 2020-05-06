import {Component, Injectable, OnInit} from '@angular/core';
import {User} from "../model/user";
import {Location} from "../model/location";
import {environment} from "../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from "../service/user.service";
declare var showMap: any;
declare var addLocationToMap: any;
@Injectable({
    providedIn: 'root'
})
@Component({
  selector: 'app-user-tracking',
  templateUrl: './user-tracking.component.html',
  styleUrls: ['./user-tracking.component.css']
})
export class UserTrackingComponent implements OnInit {
  private serverUrl = environment.mainURL + "/track";
  private stompClient;
  user: User = {
      login: "",
      fullName: "",
      password: ""
  };

  currentLocation: Location = {
      lat: 0,
      lng: 0,
      date: "",
      locationType: "",
      user: this.user
  };

    constructor(private userService: UserService) { }

  ngOnInit() {

  }

  trackUser() {
      this.userService.checkUser(this.user).subscribe(next => {
          console.log(next);
          console.log("Authorization successful")
          new showMap(53, 0);
          this.trackObserver();

      }, error => {
          console.log(error);
          alert("Authorization error!");
          console.log("Authorization error");
      });

  }
  trackObserver() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let client = this.stompClient;
    let that = this;
    client.connect({}, function (frame) {
      client.subscribe("/location/"+that.user.login, function (message) {
          that.currentLocation = {
              lat: JSON.parse(message.body).lat,
              lng: JSON.parse(message.body).lng,
              date: JSON.parse(message.body).date,
              locationType: JSON.parse(message.body).locationType,
              user: JSON.parse(message.body).user
          };
          console.log(that.currentLocation);
          new addLocationToMap(that.currentLocation);
      });
    });
  }

  trackDisconnect() {
      //TODO typeError: Cannot read property 'disconnect' of undefined
      // There is a problem with disconnect because client in trackObserver() is private so this client is not the same.

      let client = this.stompClient;
      client.disconnect(function() {
          alert("See you next time!");
      });
      console.log("Location sharing stopped");
  }
}
