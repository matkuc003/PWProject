import {Component, OnInit} from '@angular/core';
import {User} from "../model/user";
import {Location} from "../model/location";
import {Observable} from "rxjs";
import {environment} from "../../environments/environment";
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {UserService} from "../service/user.service";

@Component({
  selector: 'app-location-sharing',
  templateUrl: './location-sharing.component.html',
  styleUrls: ['./location-sharing.component.css']
})
export class LocationSharingComponent implements OnInit {
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
    locationType: "CURRENT_LOCATION",
    user: this.user
  };

  isShare: boolean = false;
  hidePassword: boolean = true;
  locationsSubscription: any;

  constructor(private userService: UserService) { }

  ngOnInit() {
    this.stompClient = Stomp.over(new SockJS(this.serverUrl));
  }

  share() {
    this.isShare = true;
    this.userService.saveUser(this.user).subscribe(next => {
      this.user = next;
    }, error => {
      console.log(error);
    });
    const locations = new Observable((observer) => {
      let watchId: number;

      if ('geolocation' in navigator) {
        watchId = navigator.geolocation.watchPosition((position: Position) => {
          observer.next(position);
        }, (error: PositionError) => {
          observer.error(error);
        });
      } else {
        observer.error('Geolocation not available');
      }

      return {
        unsubscribe() {
          navigator.geolocation.clearWatch(watchId);
        }
      };
    });

    let that = this;
    this.locationsSubscription = locations.subscribe({
      next(position: Position) {
        console.log('Current Position: ', position);
        that.currentLocation.lat = position.coords.latitude;
        that.currentLocation.lng = position.coords.longitude;
        that.currentLocation.date = position.timestamp + "";
        that.sendLocation();
      },
      error(msg) {
        console.log('Error Getting Location: ', msg);
      }
    });
  }

  sendLocation() {
    this.stompClient.send("/app/track/" + this.user.login, {}, JSON.stringify(this.currentLocation));
  }

  stopShare() {
    this.isShare = false;
    this.locationsSubscription.unsubscribe();
    this.userService.deleteUser(this.user).subscribe(next => {

    }, error => {
      console.log(error);
    });
  }
}
