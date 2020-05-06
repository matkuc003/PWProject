package pl.utp.placefinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.LocationType;
import pl.utp.placefinder.model.Place;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.repository.LocationRepository;
import pl.utp.placefinder.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import rx.*;

@Service
public class InitService {

    private PlaceService placeService;
    private UserRepository userRepository;
    private LocationRepository locationRepository;

    @Autowired
    public InitService(PlaceService placeService, UserRepository userRepository, LocationRepository locationRepository) {
        this.placeService = placeService;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    public void init() {
//        List<Place> places = placeService.makeRequest(53.1422, 17.6017, 2000, "restaurant");
//        places.forEach(System.out::println);

        User u1 = new User(0L, "janKowalski", "Jan Kowalski", "Qwerty123");
        User u2 = new User(0L, "adamNowak", "Adam Nowak", "Qwerty123");
        User u3 = new User(0L, "aniaNowicka", "Ania Nowicka", "Qwerty123");

        u1 = userRepository.save(u1);
        u2 = userRepository.save(u2);
        u3 = userRepository.save(u3);


        Location l1 = new Location(0L, 53.150849, 18.132337, LocationType.HOME, LocalDateTime.now(), u1);
        Location l2 = new Location(0L, 53.150127, 18.143287, LocationType.WORK,LocalDateTime.now(), u1);
        Location l3 = new Location(0L, 53.150712, 18.13982, LocationType.CURRENT_LOCATION, LocalDateTime.now(),u1);

        Location l4 = new Location(0L, 52.150949, 18.132337, LocationType.HOME, LocalDateTime.now(),u2);
        Location l5 = new Location(0L, 53.150127, 19.843287, LocationType.WORK, LocalDateTime.now(),u2);
        Location l6 = new Location(0L, 52.180712, 17.13982, LocationType.CURRENT_LOCATION, LocalDateTime.now(),u2);

        Location l7 = new Location(0L, 53.150849, 16.132337, LocationType.HOME, LocalDateTime.now(),u3);
        Location l8 = new Location(0L, 53.150127, 19.143287, LocationType.WORK,LocalDateTime.now(), u3);
        Location l9 = new Location(0L, 54.150712, 18.13982, LocationType.CURRENT_LOCATION,LocalDateTime.now() ,u3);

        locationRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5, l6, l7, l8, l9));


        //For testing
        //Current position of all users is changing randomly every 5 seconds
        Observable.interval(5, TimeUnit.SECONDS)
                .subscribe(aLong -> {

                    Random random = new Random();
                    locationRepository.saveAll(
                            locationRepository.findAllByLocationType(LocationType.CURRENT_LOCATION)
                                    .stream()
                                    .map(location -> {
                                        location.setLat(location.getLat() + random.nextDouble() / 10000);
                                        location.setLng(location.getLng() + random.nextDouble() / 10000);
                                        System.out.println(location);
                                        return location;
                                    })
                                    .collect(Collectors.toList())
                    );

                });
    }
}
