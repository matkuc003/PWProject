package pl.utp.placefinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.utp.placefinder.model.Location;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.repository.LocationRepository;
import pl.utp.placefinder.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class InitService {
    private UserRepository userRepository;
    private LocationRepository locationRepository;

    @Autowired
    public InitService(UserRepository userRepository, LocationRepository locationRepository) {
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
    }

    @PostConstruct
    public void init() {
        User u1 = new User(0L, "janKowalski", "Jan Kowalski", "Qwerty123");
        User u2 = new User(0L, "adamNowak", "Adam Nowak", "Qwerty123");
        User u3 = new User(0L, "aniaNowicka", "Ania Nowicka", "Qwerty123");

        u1 = userRepository.save(u1);
        u2 = userRepository.save(u2);
        u3 = userRepository.save(u3);

        Location l1 = new Location(0L, 53.150849, 18.132337, LocalDateTime.now(), u1);
        Location l2 = new Location(0L, 53.150127, 18.143287, LocalDateTime.now(), u1);
        Location l3 = new Location(0L, 53.150712, 18.13982, LocalDateTime.now(),u1);

        Location l4 = new Location(0L, 52.150949, 18.132337, LocalDateTime.now(),u2);
        Location l5 = new Location(0L, 53.150127, 19.843287, LocalDateTime.now(),u2);
        Location l6 = new Location(0L, 52.180712, 17.13982, LocalDateTime.now(),u2);

        Location l7 = new Location(0L, 53.150849, 16.132337, LocalDateTime.now(),u3);
        Location l8 = new Location(0L, 53.150127, 19.143287, LocalDateTime.now(), u3);
        Location l9 = new Location(0L, 54.150712, 18.13982, LocalDateTime.now() ,u3);

        locationRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5, l6, l7, l8, l9));
    }
}
