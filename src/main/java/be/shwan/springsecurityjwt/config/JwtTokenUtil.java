package be.shwan.springsecurityjwt.config;

import be.shwan.springsecurityjwt.modules.user.domain.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static be.shwan.springsecurityjwt.modules.user.domain.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;
import static be.shwan.springsecurityjwt.modules.user.domain.Constants.SIGNING_KEY;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    private final UserRepository userRepository;

    public String generateToken(User user) {
        return doGenerateToken(user.getUsername());
    }

    private String doGenerateToken(String subject) {
        Claims claims = Jwts.claims().subject(subject).build();
        claims.put("scope", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        return Jwts.builder()
                .claims(claims)
                .issuer("http://shwan.be:8080")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(Jwts.SIG.HS256.key().build())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken (String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimFromToken(String token) {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(SIGNING_KEY.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
        return Jwts.parser()
                .verifyWith(secretKey)
                .build().parseEncryptedClaims(token).getPayload();
    }

    public boolean validate(String token) {
        String username = getUsernameFromToken(token);
        UserDetails userDetails = userRepository.findByUsername(username);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
