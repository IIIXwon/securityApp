package be.shwan.springsecurityjwt.account.factory;

import be.shwan.springsecurityjwt.account.application.AccountService;
import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.account.domain.AccountRepository;
import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountFactory {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    public static final String DEFAULT_ACCOUNT_NAME = "testUser";
    public static final String DEFAULT_ACCOUNT_PASSWORD = "12345678";

    public Account createAccount(String username) {
        SignUpForm signUpForm = getDefaultSignUpForgetDefaultSignUpForm(username);
        return accountService.createAccount(signUpForm);
    }

    public Account findAccountByNickname(String nickname) {
        Account account = accountRepository.findByUsername(nickname);
        if (account == null) {
            return createAccount(nickname);
        }
        return account;
    }

    private SignUpForm getDefaultSignUpForgetDefaultSignUpForm(String nickname) {
        return new SignUpForm(nickname, "12345678");
    }
}
