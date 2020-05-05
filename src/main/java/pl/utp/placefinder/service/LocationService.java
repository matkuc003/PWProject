package pl.utp.placefinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.utp.placefinder.mapper.LocationMapper;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationDTO;
import pl.utp.placefinder.model.LocationType;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.repository.LocationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Location> getAllByUser(User user) {
        return locationRepository.findAllByUser(user);
    }

    public Location updateCurrentLocation(LocationDTO locationDTO) {
        User user = userService.findByLogin(locationDTO.getUser().getLogin()).get();
        locationDTO.setUser(user);
        List<Location> locationList = getAllByUser(user)
                .stream()
                .filter(location -> location.getLocationType().equals(LocationType.CURRENT_LOCATION))
                .collect(Collectors.toList());

        Location currentLocation;
        if (locationList.size() == 0) {
            currentLocation = locationMapper.convertToEntity(locationDTO);
        } else {
            currentLocation = locationList.get(0);
            currentLocation.setLng(locationDTO.getLng());
            currentLocation.setLat(locationDTO.getLat());
            currentLocation.setDate(locationMapper.mapStringToDateTime(locationDTO.getDate()));
        }

        currentLocation = updateLocation(currentLocation);
        return currentLocation;
    }
}
