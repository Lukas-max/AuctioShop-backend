package luke.shopbackend.controller.user;


import javassist.NotFoundException;
import luke.shopbackend.controller.user.service.UserService;
import luke.shopbackend.model.entity.User;
import luke.shopbackend.model.data_transfer.UserRequest;
import luke.shopbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
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
    public ResponseEntity<?> addNewUser(@Valid @RequestBody UserRequest userRequest, BindingResult result) throws NotFoundException {
        if (result.hasErrors()){
            List<String> errors = result.getAllErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.toList());

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        User savedUser = userService.addUser(userRequest);
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedUser);
    }
}
