package be.shwan.springsecurityjwt.account.application.impl;

import be.shwan.springsecurityjwt.account.application.AccountService;
import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.account.domain.AccountRepository;
import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import be.shwan.springsecurityjwt.jwt.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;
    @Override
    public Account createAccount(SignUpForm signUpForm) {
        Account account = new Account(signUpForm.username(), passwordEncoder.encode(signUpForm.password()));
        return accountRepository.save(account);
    }

    @Override
    public String login(Account account) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        return jwtTokenUtil.generateToken(account);
    }
}
