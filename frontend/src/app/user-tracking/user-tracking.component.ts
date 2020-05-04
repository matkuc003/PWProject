import {AfterViewInit, Component, OnInit} from '@angular/core';
import {User} from "../model/user";

declare var showMap: any;
declare var addPlacesOnMap: any;
declare var getMarkerLocation: any;

@Component({
  selector: 'app-user-tracking',
  templateUrl: './user-tracking.component.html',
  styleUrls: ['./user-tracking.component.css']
})
export class UserTrackingComponent implements OnInit {

  requestUserTracker: User = {
    login: "",
    fullName: "",
    password: ""
  };

  constructor() { }

  ngOnInit() {

  }

  trackUser() {
    //TODO send request about track user (checking login and password on backend) and showMap()
    new showMap(53, 0);
  }

}
