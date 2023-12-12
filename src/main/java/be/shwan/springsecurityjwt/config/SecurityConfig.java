package be.shwan.springsecurityjwt.config;

import be.shwan.springsecurityjwt.modules.user.domain.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain authorizeRequestConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/author/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/author/search").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/book/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/book/search").permitAll()
                        // TODO
                                .anyRequest().authenticated()
                );
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain userDetailConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.
                userDetailsService(userRepository::findByUsername);
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain sessionConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain corsAndCsrfConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsFilter()))
                .csrf(AbstractHttpConfigurer::disable)
        ;
        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain exceptionHandlingConfig(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                        }))
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    private CorsConfigurationSource corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
