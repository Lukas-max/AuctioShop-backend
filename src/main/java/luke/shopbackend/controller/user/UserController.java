package luke.shopbackend.controller.user;

import javassist.NotFoundException;
import luke.shopbackend.controller.user.service.UserService;
import luke.shopbackend.model.Role;
import luke.shopbackend.model.User;
import luke.shopbackend.repository.RoleRepository;
import luke.shopbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
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
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        User savedUser = userService.addUser(user);

        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedUser);
    }

    // check if username exists, if no, send empty user:
    @GetMapping(path = "/user")
    public ResponseEntity<?> getUserByName(@RequestParam(name = "username") String username) {
        return userRepository.findByUsername(username).map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new User()));
    }
}
