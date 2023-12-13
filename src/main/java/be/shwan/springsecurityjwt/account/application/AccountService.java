package be.shwan.springsecurityjwt.account.application;

import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public interface AccountService {
    Account createAccount(SignUpForm signUpForm);

    String login(Account account) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException;
}
