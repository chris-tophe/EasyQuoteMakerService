package com.christophe.quoteService.services;

import com.christophe.quoteService.models.User;
import com.christophe.quoteService.repository.UserRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = userRepository.findByUsername(username);
        if (!u.isPresent())
            throw new UsernameNotFoundException("User " + username + " not found");
        return u.get();

    }
}
