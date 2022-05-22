package account.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private JpaUserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationProvider(JpaUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName().toLowerCase();
        String rawPassword = authentication.getCredentials().toString();
        log.info("Trying to authenticate: " + username + "/" + rawPassword);

        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            var authenticated = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("READ")));
            log.info("user logged in: {}", authenticated);
            return authenticated;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}