package be.shwan.springsecurityjwt.account.application;

import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface AccountService {
    Account createAccount(SignUpForm signUpForm);
}
