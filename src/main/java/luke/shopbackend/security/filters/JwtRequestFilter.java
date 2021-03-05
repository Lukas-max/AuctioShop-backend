package luke.shopbackend.security.filters;

import luke.shopbackend.security.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "");
        if (!validateToken(token)){
            response.sendError(401, "Nie ważny token autoryzacyjny. Zaloguj się ponownie.");
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    private boolean validateToken(String token){
        String subject = null;
        String credentials = null;
        Set<GrantedAuthority> authorities = null;

        try{
            subject = jwtUtil.extractSubject(token);
            credentials = jwtUtil.extractCredentials(token);
            authorities = jwtUtil.extractAuthorities(token);
        }catch (Exception ex){
            return false;
        }

        if (subject == null || subject.isEmpty() || credentials == null || credentials.isEmpty() || authorities == null)
            return false;

        if (jwtUtil.isTokenExpired(token))
            return false;

        return true;
    }

    /**
     *
     * @return UsernamePasswordAuthenticationToken used to set Spring security context.
     */
    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        return new UsernamePasswordAuthenticationToken(
                jwtUtil.extractSubject(token),
                jwtUtil.extractCredentials(token),
                jwtUtil.extractAuthorities(token));
    }
}
