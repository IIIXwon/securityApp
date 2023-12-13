package be.shwan.springsecurityjwt.jwt;

import be.shwan.springsecurityjwt.account.domain.Account;
import be.shwan.springsecurityjwt.config.AppProperties;
import be.shwan.springsecurityjwt.jwt.dto.Header;
import be.shwan.springsecurityjwt.jwt.dto.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private static final int ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    private final ObjectMapper objectMapper;
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private final AppProperties appProperties;
    private static final SecretKey SECRET_KEY;

    static {
        try {
            SECRET_KEY = getSECRET_KEY();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKey getSECRET_KEY() throws NoSuchAlgorithmException {
        return Jwts.SIG.HS256.key().random(SecureRandom.getInstance("SHA1PRNG")).build();
    }

    public String generateTokenByJjwt(Account account){
        return Jwts.builder()
                .issuer("http://localhost:8080")
                .subject("인증된 사용자")
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .issuedAt(new Date(System.currentTimeMillis()))
                .claim("username", account.getUsername())
                .signWith(SECRET_KEY).compact();
    }

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
        String secretKey = "test";
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] encode = encoder.encode(bytes);
        log.info("secretKey : {}", secretKey);
        log.info("secret base64 encoded :{}", encoder.encodeToString(bytes));
        SecretKeySpec secretKeySpec = new SecretKeySpec(encode, "HmacSHA256");
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

    public String parseIssuer(String token) {
        Claims claims = parsePayload(token);
        return claims.getIssuer();
    }

    public Date parseExpiration(String token) {
        Claims claims = parsePayload(token);
        return claims.getExpiration();
    }

    public String parseUsername(String token) {
        Claims claims = parsePayload(token);
        return claims.get("username", String.class);
    }


    private Claims parsePayload(String token) {
        return parseToken(token).getPayload();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parser().verifyWith(SECRET_KEY).build()
                .parseSignedClaims(token);
    }

    public boolean validate(String token) {
        String issuer = parseIssuer(token);
        Date expiration = parseExpiration(token);
        return appProperties.getHost().equals(issuer) && expiration.after(new Date());
    }
}
