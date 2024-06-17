package com.javamicroservice.repositories;

import com.javamicroservice.utils.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    default void blockUserByEmail(String email) {
        Optional<User> userOptional = findByEmail(email);
        userOptional.ifPresent(user -> {
            user.setBlocked(true);
            save(user);
        });
    }

    @Transactional
    default void unblockUserByEmail(String email) {
        Optional<User> userOptional = findByEmail(email);
        userOptional.ifPresent(user -> {
            user.setBlocked(false);
            save(user);
        });
    }

    Optional<User> findByEmail(String email);
}
