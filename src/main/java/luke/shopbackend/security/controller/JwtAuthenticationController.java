package luke.shopbackend.security.controller;

import luke.shopbackend.security.JwtUtil;
import luke.shopbackend.security.model.AuthenticationRequest;
import luke.shopbackend.security.model.AuthenticationResponse;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class JwtAuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationController(
            UserService userService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(path = "/user")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        User user = userService.getUserByUsername(authenticationRequest.getUsername());

        final String jwtToken = jwtUtil.generateToken(user);

        Set<String> roles = user.getRoles()
                .stream()
                .map(r -> r.getRole().toString())
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new AuthenticationResponse(
                jwtToken,
                user.getId(),
                user.getUsername(),
                roles));
    }
}
