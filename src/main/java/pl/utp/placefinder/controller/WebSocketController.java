package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationType;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.service.LocationService;
import pl.utp.placefinder.service.UserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class WebSocketController {

    private LocationService locationService;
    private UserService userService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public WebSocketController(LocationService locationService, UserService userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.locationService = locationService;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/send/message")
    public void onReceivedMessage(String message) {
        simpMessagingTemplate.convertAndSend("/chat", new SimpleDateFormat("HH:mm:ss").format(new Date()) + message);
    }


    @MessageMapping("/send/updateUserCurrentLocation")
    void updateUserCurrentLocation(String userLogin, double lat, double lng) {
        User user = userService.findByLogin(userLogin).get();
        List<Location> locationList = locationService.getAllByUser(user)
                .stream()
                .filter(location -> location.getLocationType().equals(LocationType.CURRENT_LOCATION))
                .collect(Collectors.toList());

        Location currentLocation;
        if (locationList.size() == 0) {
            currentLocation = new Location(0L, lat, lng, LocationType.CURRENT_LOCATION, user);
        } else {
            currentLocation = locationList.get(0);
            currentLocation.setLat(lng);
            currentLocation.setLng(lat);
        }
        currentLocation = locationService.updateLocation(currentLocation);

        //TODO
        //Inform all users about location change
        simpMessagingTemplate.convertAndSend("/chat", currentLocation);
    }

}
