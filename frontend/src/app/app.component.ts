import {Component, OnInit, ViewChild} from '@angular/core';
import {Place} from "./place";
import {PlaceService} from "./place.service";
import {MatSort} from "@angular/material/sort";
import {MatTableDataSource} from "@angular/material";
import {Observable} from "rxjs";

declare var showMap: any;
declare var addPlacesOnMap: any;
declare var getMarkerLocation: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  typePlaces =  [ {value: "restaurant"}, {value: "cafe"}, {value: "church"}, {value: "dentist"}, {value: "fire_station"}, {value: "gas_station"},
    {value: "bank"}, {value: "bar"}, {value: "gym"}, {value: "hair_care"}, {value: "hospital"}, {value: "library"}, {value: "park"},
    {value: "parking"}, {value: "pharmacy"}, {value: "plumber"}, {value: "police"}, {value: "post_office"}, {value: "school"}, {value: "store"},
    {value: "supermarket"}, {value: "train_station"}];

  requestFind : RequestFindPlaces = {
    address: "",
    lat: 0,
    lng: 0,
    radius: 5000,
    type: this.typePlaces[0].value,
  };
  displayedColumns: string[] = ['name', 'address', 'rating', 'userRatingsTotal', 'openNow'];
  dataSource = null;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private placeService: PlaceService) {}

  ngOnInit(): void {
    this.getLocation();

    console.log("jestem");
    this.watchLocation();
  }

  getLocation(): void{
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition((position)=>{
        const longitude = position.coords.longitude;
        const latitude = position.coords.latitude;
        new showMap(latitude, longitude);
      }, (error) => {
        new showMap(51.505, -0.09);
      });
    } else {
      console.log("No support for geolocation")
    }
  }
  watchLocation(): void {
    // Create an Observable that will start listening to geolocation updates
// when a consumer subscribes.
    const locations = new Observable((observer) => {
      let watchId: number;

      // Simple geolocation API check provides values to publish
      if ('geolocation' in navigator) {
        watchId = navigator.geolocation.watchPosition((position: Position) => {
          observer.next(position);
        }, (error: PositionError) => {
          observer.error(error);
        });
      } else {
        observer.error('Geolocation not available');
      }

      // When the consumer unsubscribes, clean up data ready for next subscription.
      return {
        unsubscribe() {
          navigator.geolocation.clearWatch(watchId);
        }
      };
    });

// Call subscribe() to start listening for updates.
    const locationsSubscription = locations.subscribe({
      next(position) {
        console.log('Current Position: ', position);
      },
      error(msg) {
        console.log('Error Getting Location: ', msg);
      }
    });

// Stop listening for location after 10 seconds
    setTimeout(() => {
      locationsSubscription.unsubscribe();
    }, 10000);

  }

  showPlaces(places: Place[]) {
    new addPlacesOnMap(places);
  }

  searchPlaces() {
    let locationMarker = new getMarkerLocation();
    this.requestFind.lat = locationMarker.lat;
    this.requestFind.lng = locationMarker.lng;

    this.placeService.findPlaces(this.requestFind).subscribe(n => {
      this.dataSource = new MatTableDataSource(n);
      this.dataSource.sort = this.sort;
      this.showPlaces(n);
    });
  }
}

export interface RequestFindPlaces {
  address: string,
  lat: number,
  lng: number,
  radius: number,
  type: string
}
