package luke.shopbackend.security.user_details;

import luke.shopbackend.user.model.User;
import luke.shopbackend.user.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty())
            throw new UsernameNotFoundException("Nie znaleziono w bazie użytkownika: " + username);

        User user = userOptional.get();
        return new org.springframework.security.core.userdetails
                .User(user.getUsername(), user.getPassword(), getUserAuthorities(user));
    }

    private Collection<GrantedAuthority> getUserAuthorities(User user) {
        return user.getRoles()
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getRole().toString()))
                .collect(Collectors.toSet());
    }
}
