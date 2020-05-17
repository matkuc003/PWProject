package pl.utp.placefinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.placefinder.mapper.LocationMapper;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationDTO;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.repository.LocationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    private LocationRepository locationRepository;
    private UserService userService;
    private LocationMapper locationMapper;

    @Autowired
    public LocationService(LocationRepository locationRepository, UserService userService, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.userService = userService;
        this.locationMapper = locationMapper;
    }

    public List<Location> getLocations() {
        return locationRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }

    public Optional<Location> getLocation(long id) {
        return locationRepository.findById(id);
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Location location) {
        return locationRepository.save(location);
    }

    public void deleteLocation(long id) {
        locationRepository.deleteById(id);
    }

    public void deleteLocationByUser(User user) {
        List<Location> locations = locationRepository.findAllByUser(user);
        locations.stream().forEach(l->locationRepository.delete(l));
    }
    public List<Location> getAllByUser(User user) {
        return locationRepository.findAllByUser(user);
    }

    public Location addLocation(LocationDTO locationDTO) {
        User user = userService.findByLogin(locationDTO.getUser().getLogin()).get();
        locationDTO.setUser(user);

        Location location = locationMapper.convertToEntity(locationDTO);
        location = createLocation(location);

        return location;
    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point
     * @returns Distance in Meters
     */
    public double distanceBetweenLocation(double lat1, double lat2, double lon1, double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
