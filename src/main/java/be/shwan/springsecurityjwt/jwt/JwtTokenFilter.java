package be.shwan.springsecurityjwt.jwt;

import be.shwan.springsecurityjwt.account.application.AccountService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
@RequiredArgsConstructor
//public class JwtTokenFilter extends OncePerRequestFilter {
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenUtil jwtTokenUtil;
    private final AccountService accountService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (isEmpty(header) || !header.startsWith("Bearer")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        final String token = header.split(" ")[1].trim();
//        if (!jwtTokenUtil.validate(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String username = jwtTokenUtil.parseUsername(token);
//        UserDetails userDetails = accountService.loadUserByUsername(username);
//        UsernamePasswordAuthenticationToken
//                authentication = new UsernamePasswordAuthenticationToken(
//                userDetails,
//                null,
//                List.of(new SimpleGrantedAuthority("user"))
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        filterChain.doFilter(request, response);
//    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String header1 = (String) servletRequest.getAttribute("header");
//        final String header = servletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header1) || !header1.startsWith("Bearer")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        final String token = header1.split(" ")[1].trim();
        if (!jwtTokenUtil.validate(token)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String username = jwtTokenUtil.parseUsername(token);
        UserDetails userDetails = accountService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                List.of(new SimpleGrantedAuthority("user"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
