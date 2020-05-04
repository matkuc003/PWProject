package pl.utp.placefinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.utp.placefinder.model.Place;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class InitService {

    private PlaceService placeService;
    private UserRepository userRepository;

    @Autowired
    public InitService(PlaceService placeService, UserRepository userRepository) {
        this.placeService = placeService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
//        List<Place> places = placeService.makeRequest(53.1422, 17.6017, 2000, "restaurant");
//        places.forEach(System.out::println);

        User u1 = new User(0L, "janKowalski", "Jan Kowalski", "Qwerty123", "janKowalski@gmail.com");
        User u2 = new User(0L, "adamNowak", "Adam Nowak", "Qwerty123", "adamNowak@gmail.com");
        User u3 = new User(0L, "aniaNowicka", "Ania Nowicka", "Qwerty123", "aniaNowicka@gmail.com");

        u1 = userRepository.save(u1);
        u2 = userRepository.save(u2);
        u3 = userRepository.save(u3);

    }
}
