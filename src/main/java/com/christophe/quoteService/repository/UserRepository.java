package com.christophe.quoteService.repository;

import com.christophe.quoteService.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);

    List<User> findAllByUserManagerEquals(User userManager);
}
