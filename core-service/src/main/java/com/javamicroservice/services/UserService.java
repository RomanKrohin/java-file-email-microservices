package com.javamicroservice.services;

import com.javamicroservice.security.JwtUtil;
import com.javamicroservice.utils.User;
import com.javamicroservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String registerUser(User user) {
        try {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user = userRepository.save(user);
            sendWelcomeEmail(user.getEmail());
            return jwtUtil.generateToken(user.getEmail());
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    private void sendWelcomeEmail(String email) {
        System.out.println("Hello");
    }

    public String authenticate(String email, String password) {
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            return jwtUtil.generateToken(email);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public void blockUser(String email) {
        userRepository.blockUserByEmail(email);
    }

    public void unblockUser(String email) {
        userRepository.unblockUserByEmail(email);
    }

    public boolean isUserBlocked(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(User::getBlocked).orElse(false);
    }
}
