package luke.shopbackend.security.controller;

import luke.shopbackend.security.JwtUtil;
import luke.shopbackend.security.model.AuthenticationRequest;
import luke.shopbackend.security.model.AuthenticationResponse;
import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import luke.shopbackend.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;


class JwtAuthorizationControllerTest {

    @Mock
    private UserServiceImpl userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtUtil jwtUtil;
    @InjectMocks
    private JwtAuthorizationController authenticationController;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnAuthenticationResponse(){
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("lukasz","user");
        Collection<GrantedAuthority> authorities = getUserAuthorities(getUser());
        given(userService.getUserByUsername(authenticationRequest.getUsername())).willReturn(this.getUser());
        given(passwordEncoder.matches(authenticationRequest.getPassword(), "user")).willReturn(true);
        given(jwtUtil.generateJSONToken(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword(),
                authorities))).willReturn("jwtToken");
        given(jwtUtil.getExpirationDate("jwtToken"))
                .willReturn(new Date(new Date().getTime() + (1000 * 60 * 60 * 7)));

        //when
        ResponseEntity<AuthenticationResponse> entity = authenticationController.createAuthenticationToken(authenticationRequest);

        //then
        assertAll(
                () -> assertThat(entity.getStatusCodeValue(), equalTo(200)),
                () -> assertThat(entity.getBody().getJwt(), equalTo("jwtToken")),
                () -> assertThat(entity.getBody().getUserId(), equalTo(this.getUser().getId())),
                () -> assertThat(entity.getBody().getTokenExpiration(), greaterThan(new Date())),
                () -> assertThat((long) entity.getBody().getAuthorities().size(), equalTo(1L))
        );
    }


    private User getUser() {
        System.out.println();
        User user = new User();
        user.setUsername("lukasz");
        user.setPassword("user");
        user.setEmail("lukasz@o2.pl");
        user.setId(23L);
        user.getRoles().add(new Role(2L, ShopRole.USER));
        return user;
    }
    private Collection<GrantedAuthority> getUserAuthorities(User user) {
        return user.getRoles()
                .stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getRole().toString()))
                .collect(Collectors.toSet());
    }
}






























