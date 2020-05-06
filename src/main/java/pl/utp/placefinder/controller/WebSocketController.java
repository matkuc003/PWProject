package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationDTO;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.service.LocationService;
import pl.utp.placefinder.service.UserService;

@RestController
@CrossOrigin

public class WebSocketController {

    private LocationService locationService;
    private UserService userService;
    @Autowired
    public WebSocketController(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }
  @MessageMapping("/track/{login}")
    @SendTo("/location/{login}")
    public Location getLocation(@DestinationVariable String login, LocationDTO location) {
        return locationService.updateCurrentLocation(location);
    }

}
