package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationType;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.service.LocationService;
import pl.utp.placefinder.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/location")
@CrossOrigin
public class LocationController {

    private LocationService locationService;
    private UserService userService;

    @Autowired
    public LocationController(LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
    }

    @PutMapping("/updateUserCurrentLocation")
    void updateUserCurrentLocation(String userLogin, double lat, double lng) {
        User user = userService.findByLogin(userLogin).get();
        List<Location> locationList = locationService.getAllByUser(user)
                .stream()
                .filter(location -> location.getLocationType().equals(LocationType.CURRENT_LOCATION))
                .collect(Collectors.toList());

        Location currentLocation;
        if (locationList.size() == 0) {
            currentLocation = new Location(0L, lat, lng, LocationType.CURRENT_LOCATION, LocalDateTime.now(), user);
        } else {
            currentLocation = locationList.get(0);
            currentLocation.setLng(lat);
            currentLocation.setLat(lng);
        }
        currentLocation = locationService.updateLocation(currentLocation);

        //TODO
        //Inform all users about location change

    }

    @PostMapping("/addLocation")
    public void addLocation(String userLogin, @RequestBody Location location) {
        location.setUser(userService.findByLogin(userLogin).get());
        locationService.createLocation(location);
    }

    @GetMapping("/all")
    public List<Location> getAllLocations() {
        return locationService.getLocations();
    }

    @GetMapping("/allByUser")
    public List<Location> getAllLocationsByUser(String login) {
        return locationService.getAllByUser(userService.getUserByLogin(login).get());
    }

}
