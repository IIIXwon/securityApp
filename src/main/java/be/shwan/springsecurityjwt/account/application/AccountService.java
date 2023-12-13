package be.shwan.springsecurityjwt.account.application;

import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.NoSuchAlgorithmException;


public interface AccountService extends UserDetailsService {
    Account createAccount(SignUpForm signUpForm);

    String login(String username);

}
