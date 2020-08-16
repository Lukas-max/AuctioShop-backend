package luke.shopbackend.jwt_security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {
    private final String SECRET = "secret";

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        userDetails.getAuthorities().forEach(d -> claims.put("Role", d.getAuthority()));
        return createToken(claims, userDetails.getUsername());
    }

//    claims to środkowa częśc tokena
    private String createToken(Map<String, Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 60)))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationDate(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        System.out.println(claims.toString());
        return claimsResolver.apply(claims);
    }

    private boolean isTokenExpired(String token){
        return extractExpirationDate(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
