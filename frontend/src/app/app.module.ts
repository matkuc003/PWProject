import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from "./material-module";
import {HttpClientModule} from "@angular/common/http";
import {FormsModule} from "@angular/forms";
import {LocationSharingComponent} from './location-sharing/location-sharing.component';
import {UserTrackingComponent} from './user-tracking/user-tracking.component';

@NgModule({
  declarations: [
    AppComponent,
    LocationSharingComponent,
    UserTrackingComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
