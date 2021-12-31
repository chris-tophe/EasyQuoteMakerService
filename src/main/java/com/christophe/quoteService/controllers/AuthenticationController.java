package com.christophe.quoteService.controllers;

import com.christophe.quoteService.component.TokenUtils;
import com.christophe.quoteService.models.AuthenticationRequest;
import com.christophe.quoteService.models.AuthenticationResponse;
import com.christophe.quoteService.models.User;
import com.christophe.quoteService.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/login")

public class AuthenticationController {

    final UserService userService;

    final AuthenticationManager authenticationManager;

    private final TokenUtils tokenUtils;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager,
            TokenUtils tokenUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping()
    ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Objects.requireNonNull(authenticationRequest.getUsername());
            Objects.requireNonNull(authenticationRequest.getPassword());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (DisabledException e) {
            // throw new Exception();
            return new ResponseEntity<String>("USER_DISABLED", HttpStatus.UNAUTHORIZED);
        } catch (BadCredentialsException e) {
            // throw new Exception("INVALID_CREDENTIALS");
            return new ResponseEntity<String>("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
        } catch (LockedException e) {
            // throw new Exception("USER_LOCKED");
            return new ResponseEntity<String>("USER_LOCKED", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unknown error", HttpStatus.NOT_ACCEPTABLE);
        }
        final User u = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = tokenUtils.generateToken(u);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

}
