import {Component, OnInit} from '@angular/core';
import {User} from "../model/user";
import {Location} from "../model/location";
import {environment} from "../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

declare var showMap: any;
declare var addLocationToMap: any;

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

  constructor() { }

  ngOnInit() {

  }

  trackUser() {
    //TODO send request about track user (checking login and password on backend)
    new showMap(53, 0);
    this.trackObserver();
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
      this.stompClient.disconnect("/location/" + this.user.login);
  }
}
