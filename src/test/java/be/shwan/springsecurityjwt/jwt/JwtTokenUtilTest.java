package be.shwan.springsecurityjwt.jwt;

import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.config.AppProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtTokenUtilTest {
    @Test
    void generateToken() throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Account account = new Account("user", "1234");
        ObjectMapper objectMapper = new ObjectMapper();
        AppProperties appProperties = new AppProperties();
        appProperties.setHost("http://localhost:8080");
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(objectMapper,appProperties);
        String result = jwtTokenUtil.generateToken(account);
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InVzZXIifQ.reus_xGT7dxZ2TU3tF86JSqEgEpaydK-JDVOGXvIHlk", result);
    }

    @Test
    void generateTokenV2() {
        Account account = new Account("user", "1234");
        ObjectMapper objectMapper = new ObjectMapper();
        AppProperties appProperties = new AppProperties();
        appProperties.setHost("http://localhost:8080");
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(objectMapper, appProperties);
        String token = jwtTokenUtil.generateTokenByJjwt(account);
        System.out.println("result = " + token);
        String result = jwtTokenUtil.parseIssuer(token);
        assertEquals("http://localhost:8080", result );
        assertTrue(jwtTokenUtil.validate(token));
    }
}