package luke.shopbackend.security.controller;

import luke.shopbackend.security.JwtUtil;
import luke.shopbackend.security.model.AuthenticationRequest;
import luke.shopbackend.security.model.AuthenticationResponse;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class JwtAuthenticationController {

    private final UserServiceImpl userServiceImpl;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationController(
            UserServiceImpl userServiceImpl,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/user")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) {

        User user = userServiceImpl.getUserByUsername(authenticationRequest.getUsername());
        Collection<GrantedAuthority> authorities = getUserAuthorities(user);

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Hasło albo nazwa użytkownika nie prawidłowe.");

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword(),
                        authorities
                );

        authenticationManager.authenticate(token);
        final String jwtToken = jwtUtil.generateToken(user);

        return ResponseEntity.ok(new AuthenticationResponse(
                jwtToken,
                user.getId(),
                user.getUsername(),
                authorities));
    }

    /**
     *
     * @param user -> User entity class. Contains id, username, password and email.
     * @return Collection of GrantedAuthorities from Role entity ShopRole class.
     */
    private Collection<GrantedAuthority> getUserAuthorities(User user) {
        return user.getRoles()
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getRole().toString()))
                .collect(Collectors.toSet());
    }
}
