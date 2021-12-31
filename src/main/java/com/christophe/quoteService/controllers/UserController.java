package com.christophe.quoteService.controllers;

import com.christophe.quoteService.component.TokenUtils;
import com.christophe.quoteService.component.UserValidator;
import com.christophe.quoteService.models.Role;
import com.christophe.quoteService.models.User;
import com.christophe.quoteService.models.UserDto;
import com.christophe.quoteService.repository.UserRepository;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping(value = "/user")
public class UserController {

    UserRepository userRepository;

    TokenUtils tokenUtils;

    UserValidator userValidator;

    ModelMapper modelMapper;

    @Autowired
    public UserController(UserRepository userRepository, TokenUtils tokenUtils, UserValidator userValidator,
            ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.tokenUtils = tokenUtils;
        this.userValidator = userValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping()
    ResponseEntity<User> addUser(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        /*
         * if (!userValidator.isValid(user)) {
         * return ResponseEntity.unprocessableEntity().build();
         * }
         */
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.getRoles().add(Role.USER);
        var savedUser = userRepository.save(user);
        if (savedUser.getId() == null) {
            return ResponseEntity.unprocessableEntity().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(String.format("/%d", savedUser.getId()))
                .build()
                .toUri();
        return ResponseEntity.created(location).body(savedUser);
    }

    @GetMapping()
    ResponseEntity<User> getMe(Principal authUser) {
        var user = userRepository.findByUsername(authUser.getName());
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/count")
    ResponseEntity<Long> getCount() {
        return ResponseEntity.ok(userRepository.count());
    }

    @GetMapping(value = "/all")
    List<User> getAllFromTo(@RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "0") int elements) {
        if (elements == 0) {
            elements = (int) userRepository.count();
        }
        return userRepository.findAll(PageRequest.of(page, elements)).get()
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/id/{id}")
    ResponseEntity<User> getById(@PathVariable(value = "id") long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    ResponseEntity<User> updateUser(@RequestBody User user) {
        var dbUser = userRepository.findById(user.getId());
        if (dbUser.isPresent()) {
            var savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping(value = "/getManaged/{id}")
    ResponseEntity<List<UserDto>> getManagedByUser(@PathVariable(value = "id") long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            var managedUserList = userRepository.findAllByUserManagerEquals(user.get());
            return ResponseEntity.ok(managedUserList.stream()
                    .map((u) -> modelMapper.map(u, UserDto.class))
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }
}
