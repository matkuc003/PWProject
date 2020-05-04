package pl.utp.placefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.utp.placefinder.model.User;
import pl.utp.placefinder.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user) {

        String login = user.getLogin();
        String name = user.getName();
        String password = user.getPassword();
        String email = user.getEmail();

        if (login.equals("")
                || userService.getUsers().stream().map(User::getLogin).anyMatch(s -> s.equals(login))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (name.equals("") || password.equals("")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
                || userService.getUsers().stream().map(User::getLogin).anyMatch(s -> s.equals(email))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.createUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}