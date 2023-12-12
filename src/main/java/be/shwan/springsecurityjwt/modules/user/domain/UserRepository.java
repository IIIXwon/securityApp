package be.shwan.springsecurityjwt.modules.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRepository {

    private final PasswordEncoder passwordEncoder;
    public UserDetails findByUsername(String username) {
        return new User(username, passwordEncoder.encode("1234"), List.of(new SimpleGrantedAuthority("USER")));
    }

}
