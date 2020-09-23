package luke.shopbackend.user;


import luke.shopbackend.user.service.UserService;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.model.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();

        if (users == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not fetch users from database");
        else if (users.isEmpty())
            return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
        else
            return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody UserRequest userRequest) {
        User savedUser = userService.addUser(userRequest);

        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedUser);
    }
}
