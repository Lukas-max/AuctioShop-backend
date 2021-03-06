package luke.shopbackend.user;

import luke.shopbackend.order.model.entity.CustomerOrder;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.model.UserRequest;
import luke.shopbackend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Page<CustomerOrder>> getUserWithAllDataById(
            @PathVariable Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerOrder> orders = userService.findUserAndOrderWithDataByUserId(id, pageable);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id){
        userService.deleteUserAndAllUserDataByUserId(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody UserRequest userRequest,
            UriComponentsBuilder uriComponentsBuilder) {

        User savedUser = userService.addUser(userRequest);
        URI locationUri = uriComponentsBuilder
                .path("/api/users/{id}")
                .build(savedUser.getId());

        return ResponseEntity.created(locationUri).body(savedUser);
    }
}
