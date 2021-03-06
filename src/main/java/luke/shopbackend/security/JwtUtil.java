package luke.shopbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtUtil {

    @Value("${Shop.token}")
    private String SECRET_KEY;
    private final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    public String generateJSONToken(Authentication authentication) {
        Claims claims = generateClaims(authentication);
        return createToken(authentication, claims);
    }

    private Claims generateClaims(Authentication authentication){
        String authorities = getAuthorities(authentication);
        Claims claims = Jwts.claims();
        claims.put("authority", authorities);
        claims.put("credentials", authentication.getCredentials().toString());
        return claims;
    }

    private String getAuthorities(Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String createToken(Authentication authentication, Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String extractSubject(String token) {
        return extractClaim(token).getSubject();
    }

    public String extractCredentials(String token){
        return extractClaim(token).get("credentials", String.class);
    }

    public Set<GrantedAuthority> extractAuthorities(String token){
        String[] scope = extractClaim(token)
                .get("authority", String.class)
                .split(",");

        return Arrays.stream(scope)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    public Date getExpirationDate(String token) {
        return extractClaim(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractClaim(token)
                    .getExpiration()
                    .before(new Date());

        }catch (Exception ex){
            return false;
        }
    }

    private Claims extractClaim(String token) {
        Claims claims = null;

        try{
            claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

        }catch (Exception ex){
            log.warn("Login attempt with invalid Json Web token.");
        }
        return claims;
    }
}
