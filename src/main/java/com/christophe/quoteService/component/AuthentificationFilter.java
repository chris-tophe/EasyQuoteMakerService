package com.christophe.quoteService.component;

import com.christophe.quoteService.models.User;
import com.christophe.quoteService.services.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
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
public class AuthentificationFilter extends OncePerRequestFilter {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestToken = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (requestToken != null && requestToken.startsWith("Bearer ")) {
            token = jwtUtils.bearerRemove(requestToken);
            try {
                username = jwtUtils.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.warn("Unable to get Token");
            } catch (ExpiredJwtException e) {
                logger.warn("Token has expired");
            }
        } else {
            logger.warn("Token does not begin with Bearer");
        }
        if (username != null) {
            User user =  userService.loadUserByUsername(username);
            if (jwtUtils.validateToken(token, user)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), null, user.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
