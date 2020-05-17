package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.service.LocationService;
import pl.utp.placefinder.service.UserService;

import java.util.List;

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

    @GetMapping("/distanceBetweenLocation")
    public double distanceBetweenLocation(double lat1, double lat2, double lon1, double lon2) {
        return locationService.distanceBetweenLocation(lat1, lat2, lon1, lon2);
    }

}
