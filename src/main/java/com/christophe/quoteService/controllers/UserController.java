package com.christophe.quoteService.controllers;

import com.christophe.quoteService.component.TokenUtils;
import com.christophe.quoteService.component.UserValidator;
import com.christophe.quoteService.models.Role;
import com.christophe.quoteService.models.User;
import com.christophe.quoteService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.security.Principal;

@Slf4j
@RestController
@CrossOrigin(origins="*",allowedHeaders = "*",allowCredentials = "true")
@RequestMapping(value = "/user")
public class UserController {

    UserRepository userRepository;

    TokenUtils tokenUtils;

    UserValidator userValidator;

    @Autowired
    public UserController(UserRepository userRepository, TokenUtils tokenUtils, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.userValidator = userValidator;
    }

    @PostMapping()
    ResponseEntity<User> addUser(@RequestBody User user){
        if (user == null) return ResponseEntity.noContent().build();
        if (! userValidator.isValid(user)) return ResponseEntity.unprocessableEntity().build();
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.getRoles().add(Role.USER);
        User u = userRepository.save(user);
        if (u.getId() == null) return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(u);
    }

    @GetMapping()
    User getMe(Principal authUser) {
        log.trace(authUser.toString());
        return  userRepository.findByUsername(authUser.getName());
    }

}
