package be.shwan.springsecurityjwt.controller;

import be.shwan.springsecurityjwt.account.dto.SignUpForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloRestControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void post22() throws Exception {
        mockMvc.perform(post("/hello/user").with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void signUp() throws Exception {
        SignUpForm signUpForm = new SignUpForm("1231", "12312");
        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpForm))
//                .with(csrf()))
                )
                .andExpect(status().isOk())
        ;
    }
}