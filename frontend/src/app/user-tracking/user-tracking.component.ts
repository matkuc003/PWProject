import {Component, Injectable, OnInit} from '@angular/core';
import {User} from "../model/user";
import {Location} from "../model/location";
import {environment} from "../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from "../service/user.service";

declare var showMap: any;
declare var addLocationToMap: any;
declare var removeMarker: any;

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

  isTracking: boolean = false;
  firstShowMap: boolean = false;

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
          if(!this.firstShowMap) {
            new showMap(53, 0);
            this.firstShowMap = true;
          }
          this.trackObserver();
      }, error => {
          alert("Authorization error!");
      });
  }

  trackObserver() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let client = this.stompClient;
    let that = this;
    client.connect({}, function (frame) {
      client.subscribe("/location/"+that.user.login, function (message) {
          if(message.body == "end") {
              that.trackDisconnect();
          } else {
              that.currentLocation = {
                  lat: JSON.parse(message.body).lat,
                  lng: JSON.parse(message.body).lng,
                  date: JSON.parse(message.body).date,
                  locationType: JSON.parse(message.body).locationType,
                  user: JSON.parse(message.body).user
              };
              console.log(that.currentLocation);
              new addLocationToMap(that.currentLocation);
          }
      });
    });
    this.isTracking = true;
  }

  trackDisconnect() {
    this.stompClient.disconnect("/location/" + this.user.login);
    this.isTracking = false;
    new removeMarker();
  }
}
