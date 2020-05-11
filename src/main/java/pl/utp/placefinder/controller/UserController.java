package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.service.LocationService;
import pl.utp.placefinder.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private UserService userService;
    private LocationService locationService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public UserController(UserService userService, LocationService locationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.userService = userService;
        this.locationService = locationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{login}")
    public ResponseEntity<User> getUser(@PathVariable("login") String login) {
        Optional<User> user = userService.getUserByLogin(login);

        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        String login = user.getLogin();
        String fullName = user.getFullName();
        String password = user.getPassword();

        if (login.equals("")
                || userService.getUsers().stream().map(User::getLogin).anyMatch(s -> s.equals(login))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (fullName.equals("") || password.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.createUser(new User(0l, login, fullName, password));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @PostMapping("/check")
    public ResponseEntity<Boolean> checkUser(@RequestBody User user)
    {
        String passwordToCheck = user.getPassword();
        String loginToCheck = user.getLogin();
        Optional<User> userInDB = userService.getUserByLogin(loginToCheck);
        if(!userInDB.isPresent())
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);

        if(userInDB.get().getLogin().equals(loginToCheck) && userInDB.get().getPassword().equals(passwordToCheck))
            return new ResponseEntity<>(true,HttpStatus.OK);
        else
            return new ResponseEntity<>(false,HttpStatus.EXPECTATION_FAILED);
    }
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(@RequestBody User user)
    {
        try{
            User userToDelete = userService.getUserByLogin(user.getLogin()).get();
            locationService.deleteLocationByUser(userToDelete);
            long userId = userService.getUserByLogin(user.getLogin()).get().getId();
            userService.deleteUser(userId);
            simpMessagingTemplate.convertAndSend("/location/"+user.getLogin(), "end");
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        catch(RuntimeException e){
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.EXPECTATION_FAILED);
        }
    }
}
