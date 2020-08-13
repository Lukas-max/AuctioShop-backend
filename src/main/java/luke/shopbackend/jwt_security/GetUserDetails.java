package luke.shopbackend.jwt_security;

import luke.shopbackend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GetUserDetails implements UserDetails {
    private String username;
    private String password;
    private Set<GrantedAuthority> authorities = new HashSet<>();

    public GetUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        user.getRoles().forEach(u -> authorities.add(new SimpleGrantedAuthority(u.getRole().toString())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>(this.authorities);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
