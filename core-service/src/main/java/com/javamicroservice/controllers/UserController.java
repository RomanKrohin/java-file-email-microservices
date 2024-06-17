package com.javamicroservice.controllers;

import com.javamicroservice.utils.RabbitMQSender;
import com.javamicroservice.utils.User;
import com.javamicroservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            String token = userService.registerUser(user);
            rabbitMQSender.sendAuthMessage(user.getEmail(), "Success registration");
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            String token = userService.authenticate(email, password);
            rabbitMQSender.sendAuthMessage(email, "Success log in");
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/mod/block-user")
    public ResponseEntity<String> blockUser(@RequestParam String email) {
        userService.blockUser(email);
        return ResponseEntity.ok("User " + email + " has been blocked");
    }

    @PostMapping("/mod/unblock-user")
    public ResponseEntity<String> unblockUser(@RequestParam String email) {
        userService.unblockUser(email);
        return ResponseEntity.ok("User " + email + " has been unblocked");
    }
}
