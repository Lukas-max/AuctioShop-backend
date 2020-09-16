package luke.shopbackend.controller.authentication;

import luke.shopbackend.jwt_security.JwtUtil;
import luke.shopbackend.model.data_transfer.AuthenticationRequest;
import luke.shopbackend.model.data_transfer.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("jpaUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(path = "/user")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception{

       try {
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           authenticationRequest.getUsername(),
                           authenticationRequest.getPassword())
           );
       }catch (BadCredentialsException e){
           throw new Exception("Incorrect username or password", e);
       }

       //granted Authorities: ROLE_ADMIN, ROLE_USER, tutaj:
       final UserDetails userDetails = userDetailsService.loadUserByUsername(
               authenticationRequest.getUsername());

       final String jwtToken = jwtUtil.generateToken(userDetails);
       Set<String> roles = userDetails
               .getAuthorities()
               .stream()
               .map(Object::toString).collect(Collectors.toSet());

       return ResponseEntity.ok(new AuthenticationResponse(
               jwtToken,
               userDetails.getUsername(),
               roles));
    }
}
