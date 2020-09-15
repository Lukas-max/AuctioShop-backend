package luke.shopbackend.controller.user.service;

import javassist.NotFoundException;
import luke.shopbackend.model.Role;
import luke.shopbackend.model.User;
import luke.shopbackend.repository.RoleRepository;
import luke.shopbackend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers(){
        List<User> allUsers = new ArrayList<>();
        userRepository.findAll().forEach(allUsers::add);
        return allUsers;
    }

    public User addUser(User user){
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No user data.");
        if (user.getId() != null)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User with id not allowed for new user creation");


        Optional<Role> optional = roleRepository.findById(2L);
        Role role = null;
        if (optional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not find user roles");
        }

        role = optional.get();
        user.getRoles().add(role);
        String encryptPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPass);

        return userRepository.save(user);
    }
}
