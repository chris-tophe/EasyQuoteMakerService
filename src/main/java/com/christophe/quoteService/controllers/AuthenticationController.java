package com.christophe.quoteService.controllers;

import com.christophe.quoteService.component.JwtUtils;
import com.christophe.quoteService.models.AuthenticationRequest;
import com.christophe.quoteService.models.AuthenticationResponse;
import com.christophe.quoteService.models.User;
import com.christophe.quoteService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping()
    ResponseEntity<?> loginUser(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        Objects.requireNonNull(authenticationRequest.getUsername());
        Objects.requireNonNull(authenticationRequest.getPassword());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS");
        } catch (LockedException e) {
            throw new Exception("USER_LOCKED");
        }
        final User u = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtils.generateToken(u);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

}
