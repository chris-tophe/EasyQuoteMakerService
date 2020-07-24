package com.christophe.quoteService.controllers;

import com.christophe.quoteService.component.JwtUtils;
import com.christophe.quoteService.models.Role;
import com.christophe.quoteService.models.User;
import com.christophe.quoteService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import javax.servlet.http.HttpServletRequest;
import java.net.URI;


@RestController
@CrossOrigin(origins="*",allowedHeaders = "*",allowCredentials = "true")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;


    @PostMapping()
    ResponseEntity<User> addUser(@RequestBody User user, HttpServletRequest request, @AuthenticationPrincipal User authUser){
        User u = userRepository.save(user);
        if (u == null) return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(u);
    }

    @GetMapping()
    User getMe(HttpServletRequest request) {
        String token = jwtUtils.getTokenFromRequest(request);
        User u = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token));
        return u;
    }

    @GetMapping(value = "/all")
    Iterable<User> getUsers(){
        return userRepository.findAll();
    }


}
