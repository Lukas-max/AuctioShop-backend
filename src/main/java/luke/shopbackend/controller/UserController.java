package luke.shopbackend.controller;

import javassist.NotFoundException;
import luke.shopbackend.model.Role;
import luke.shopbackend.model.User;
import luke.shopbackend.repository.RoleRepository;
import luke.shopbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> addUser(@RequestBody User user) throws NotFoundException {
        Optional<Role> optional = roleRepository.findById(2L);
        Role role = null;
        if (optional.isEmpty()){
            throw new NotFoundException("Didn't found Role object in database");
        }

        role = optional.get();
        user.getRoles().add(role);
        String encryptPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPass);
        User savedUser = userRepository.save(user);

        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(locationUri).body(savedUser);
    }

    // check if username exists, if no, send empty user:
    @GetMapping(path = "user={name}")
    public ResponseEntity<?> getUserByName(@PathVariable("name") String username){
        return userRepository.findByUsername(username).map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new User()));
    }
}
