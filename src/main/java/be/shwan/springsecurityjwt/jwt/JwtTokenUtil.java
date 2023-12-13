package be.shwan.springsecurityjwt.jwt;

import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.jwt.dto.Header;
import be.shwan.springsecurityjwt.jwt.dto.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final ObjectMapper objectMapper;
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    public String generateToken(Account account) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        String header = createHeader();
        String payload = createPayload(account);
        String signature = createSignature(header, payload);
        log.info("header : {}", header);
        log.info("payload : {}", payload);
        log.info("signature : {}", signature);
        return header +
                "." +
                payload +
                "." +
                signature;
    }

    private String createSignature(String byteHeader, String bytePayload) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha_HMAC = Mac.getInstance("HmacSHA256");
        String secretKey = "secret";
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(bytes, "HmacSHA256");
//        byte[] encodeSecretKey = encoder.encode(bytes);
//        SecretKeySpec secretKeySpec = new SecretKeySpec(encodeSecretKey, "HmacSHA256");
        sha_HMAC.init(secretKeySpec);
        String message = byteHeader + "." + bytePayload;
        byte[] encodeMessage = sha_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return encoder.encodeToString(encodeMessage);
    }

    private String createPayload(Account account) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(new Payload(account.getUsername()));
        return encoder.encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    private String createHeader() throws JsonProcessingException {
        String header = objectMapper.writeValueAsString(new Header("HS256", "JWT"));
        return encoder.encodeToString(header.getBytes(StandardCharsets.UTF_8));
    }
}
