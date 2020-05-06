package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public UserController(UserService userService, LocationService locationService) {
        this.userService = userService;
        this.locationService=locationService;
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
        User userInDB = userService.getUserByLogin(loginToCheck).get();
        if(userInDB.getLogin().equals(loginToCheck) && userInDB.getPassword().equals(passwordToCheck))
        {
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(false,HttpStatus.EXPECTATION_FAILED);
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteUser(@RequestBody User user)
    {

        try{
            User userToDelete = userService.getUserByLogin(user.getLogin()).get();
            locationService.deleteLocationByUser(userToDelete);
            long userId = userService.getUserByLogin(user.getLogin()).get().getId();
            userService.deleteUser(userId);
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        catch(RuntimeException e){
            e.printStackTrace();
            return new ResponseEntity<>(false,HttpStatus.EXPECTATION_FAILED);

        }

    }
}
