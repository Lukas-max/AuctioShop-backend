package luke.shopbackend.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class MySecurityContextFactory implements WithSecurityContextFactory<CustomUser> {

    @Override
    public SecurityContext createSecurityContext(CustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("bill", null,
                        List.of(customUser::authority));
        context.setAuthentication(authentication);
        return context;
    }
}
