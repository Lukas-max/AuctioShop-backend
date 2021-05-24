package luke.shopbackend.security.filters;

import luke.shopbackend.security.JwtUtil;
import luke.shopbackend.user.enums.ShopRole;
import luke.shopbackend.user.model.Role;
import luke.shopbackend.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JwtRequestFilterTest {

    @Mock
    JwtUtil jwtUtil;
    @Mock
    HttpServletRequest servletRequest;
    @Mock
    HttpServletResponse servletResponse;
    @Mock
    FilterChain filterChain;
    @Mock
    SecurityContextHolder contextHolder;
    @InjectMocks
    JwtRequestFilter jwtRequestFilter;

    @BeforeEach
    public void setupMocks(){
        MockitoAnnotations.initMocks(this);
    }


    /**
     * If the header is null the the method should return the filter chain and stop token validation.
     */
    @Test
    void shouldNotInvokeTokenValidationIfTokenIsNull() throws IOException, ServletException {
        // given
        given(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn(null);
        // when
        jwtRequestFilter.doFilterInternal(servletRequest, servletResponse, filterChain);
        // then
        then(jwtUtil).should(times(0)).extractSubject("token");
        then(jwtUtil).should(times(0)).extractCredentials("token");
        then(jwtUtil).should(times(0)).extractAuthorities("token");
    }

    /**
     * If the header is invalid the the method should return the filter chain and stop token validation.
     */
    @Test
    void shouldNotInvokeTokenValidationIfTokenHeaderIsInvalid() throws IOException, ServletException {
        // given
        given(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("invalid header");
        // when
        jwtRequestFilter.doFilterInternal(servletRequest, servletResponse, filterChain);
        // then
        then(jwtUtil).should(times(0)).extractSubject("token");
        then(jwtUtil).should(times(0)).extractCredentials("token");
        then(jwtUtil).should(times(0)).extractAuthorities("token");
    }

    @Test
    void shouldInvokeTokenValidationIfTokenHeaderValid() throws IOException, ServletException {
        // given
        given(servletRequest.getHeader(HttpHeaders.AUTHORIZATION)).willReturn("Bearer dsfsdf3423");
        // when
        jwtRequestFilter.doFilterInternal(servletRequest, servletResponse, filterChain);
        // then
        then(jwtUtil).should(times(0)).extractSubject("token");
        then(jwtUtil).should(times(0)).extractCredentials("token");
        then(jwtUtil).should(times(0)).extractAuthorities("token");
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











