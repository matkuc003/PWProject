package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationDTO;
import pl.utp.placefinder.service.LocationService;

@RestController
@CrossOrigin

public class WebSocketController {
    private LocationService locationService;

    @Autowired
    public WebSocketController(LocationService locationService) {
        this.locationService = locationService;
    }

    @MessageMapping("/track/{login}")
    @SendTo("/location/{login}")
    public Location getLocation(@DestinationVariable String login, LocationDTO location) {
        return locationService.addLocation(location);
    }

}
