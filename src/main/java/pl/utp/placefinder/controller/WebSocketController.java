package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.placefinder.mapper.LocationMapper;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationDTO;
import pl.utp.placefinder.service.LocationService;
import pl.utp.placefinder.service.UserService;

@RestController
@CrossOrigin
public class WebSocketController {

    private LocationService locationService;
    private UserService userService;
    private SimpMessagingTemplate simpMessagingTemplate;
    private LocationMapper locationMapper;

    @Autowired
    public WebSocketController(LocationService locationService, UserService userService, SimpMessagingTemplate simpMessagingTemplate, LocationMapper locationMapper) {
        this.locationService = locationService;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.locationMapper = locationMapper;
    }

    @MessageMapping("/track/{login}")
    @SendTo("/location/{login}")
    public Location getLocation(@DestinationVariable String login, LocationDTO location) {
        //TODO save location to database
        return locationMapper.convertToEntity(location);
    }
}
