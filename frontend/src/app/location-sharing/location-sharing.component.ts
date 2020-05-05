import {Component, OnInit} from '@angular/core';
import {User} from "../model/user";
import {Location} from "../model/location";
import {Observable} from "rxjs";
import {UserService} from "../service/user.service";

@Component({
  selector: 'app-location-sharing',
  templateUrl: './location-sharing.component.html',
  styleUrls: ['./location-sharing.component.css']
})
export class LocationSharingComponent implements OnInit {

  user: User = {
    login: "",
    fullName: "",
    password: ""
  };

  currentLocation: Location = {
    lat: 0,
    lng: 0,
    date: "",
    user: this.user
  };

  isShare: boolean = false;
  hidePassword: boolean = true;
  locationsSubscription: any;

  constructor(private userService: UserService) { }

  ngOnInit() {
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
    // TODO send location to backend
  }

  stopShare() {
    this.isShare = false;
    this.locationsSubscription.unsubscribe();
  }
}
