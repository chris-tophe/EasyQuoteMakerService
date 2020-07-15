package com.christophe.quoteService.controllers;

import com.christophe.quoteService.models.User;
import com.christophe.quoteService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping(value = "/add")
    ResponseEntity<User> addUser(@RequestBody User user){
        User u = userRepository.save(user);
        if (u == null) return ResponseEntity.noContent().build();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(u.getId())
                .toUri();
        return ResponseEntity.created(location).body(u);
    }

    @GetMapping(value = "/all")
    Iterable<User> getUser(){
        return userRepository.findAll();
    }

    @GetMapping(value = "/format")
    User blanckUser(){
        return new User("Christophe","LOYER","christophe.loyer@gmail.com");
    }

}
