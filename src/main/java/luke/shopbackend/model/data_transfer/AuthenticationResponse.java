package luke.shopbackend.model.data_transfer;

import java.util.HashSet;
import java.util.Set;


public class AuthenticationResponse {
    private final String jwt;
    private final String username;
    private final Set<String> roles;

    public AuthenticationResponse(String jwt, String username, Set<String> roles) {
        this.jwt = jwt;
        this.username = username;
        this.roles = new HashSet<>(roles);
    }

    public String getJwt() {
        return jwt;
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
