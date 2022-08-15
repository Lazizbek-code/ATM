package uz.pdp.appatm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.appatm.service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtFilet extends OncePerRequestFilter {
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            authorization = authorization.substring(7);
            String username = jwtProvider.getUsernameFromToken(authorization);

            if (username != null) {
                UserDetails userDetails = authService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        } else if (authorization != null && authorization.startsWith("Basic")) {
            String basic = authorization.substring(6);

            byte[] decode = Base64.getDecoder().decode(basic);
            String str = new String(decode, StandardCharsets.UTF_8);
            String[] usernamePassword = str.split(":");
            String spacialCode = usernamePassword[0];
            String pinCode = usernamePassword[1];
            UserDetails userDetails = authService.loadCardBySpecialCode(spacialCode);
            if (passwordEncoder.matches(pinCode, userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
