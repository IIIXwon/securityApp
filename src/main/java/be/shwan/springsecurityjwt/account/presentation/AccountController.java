package be.shwan.springsecurityjwt.account.presentation;

import be.shwan.springsecurityjwt.account.application.AccountService;
import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.account.domain.AccountRepository;
import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountRepository accountRepository;
//    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpForm signUpForm) {
        log.info("signUpForm : {}", signUpForm.toString());
        Account account = accountService.createAccount(signUpForm);
        return ResponseEntity.ok().body("계정을 생성했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SignUpForm loginForm) {
        log.info("signUpForm : {}", loginForm.toString());
        Account account = accountRepository.findByUsername(loginForm.username());
        if (account == null) {
            return ResponseEntity.badRequest().body("ip,password를 확인하세요");
        } else {
            return ResponseEntity.ok().body("로그인 되었습니다.");
        }
    }
}