package com.christophe.quoteService.component;

import com.christophe.quoteService.models.User;
import com.christophe.quoteService.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    UserService userService;

    @Autowired
    TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader("Authorization");
        if (requestToken != null) {
            String token = null;
            String username = null;
            if (requestToken.startsWith("Bearer ")) {
                token = tokenUtils.bearerRemove(requestToken);
                try {
                    username = tokenUtils.getUsernameFromToken(token);
                } catch (IllegalArgumentException e) {
                    log.warn("Unable to get Token");
                } catch (ExpiredJwtException e) {
                    log.warn("Token has expired");
                }
            } else {
                log.trace("Token does not begin with Bearer");
            }
            if (username != null) {
                User user = userService.loadUserByUsername(username);
                if (tokenUtils.validateToken(token, user)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            user.getUsername(), null, user.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    log.trace("User {} identified by token", user.getUsername());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
