package be.shwan.springsecurityjwt.account.presentation;

import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import be.shwan.springsecurityjwt.account.factory.AccountFactory;
import be.shwan.springsecurityjwt.config.MockMvcTest;
import be.shwan.springsecurityjwt.config.TestContainers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class AccountControllerTest extends TestContainers {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountFactory accountFactory;

    @Test
    void signUp() throws Exception {
        SignUpForm signUpForm = new SignUpForm("user", "1234");
        mockMvc.perform(post("/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(signUpForm)))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void login() throws Exception {
        SignUpForm signUpForm = new SignUpForm(AccountFactory.DEFAULT_ACCOUNT_NAME, AccountFactory.DEFAULT_ACCOUNT_PASSWORD);
        accountFactory.createAccount(AccountFactory.DEFAULT_ACCOUNT_NAME);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(signUpForm)))
                .andExpect(status().isOk())
        ;
    }
}