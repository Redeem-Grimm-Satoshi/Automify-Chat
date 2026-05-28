package com.automifychat.app.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;

    public SignupService(UserAccountRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount signup(String username, String email, String rawPassword) {
        if (users.existsByUsername(username)) {
            throw new SignupException("Username already taken");
        }
        if (users.existsByEmail(email)) {
            throw new SignupException("Email already registered");
        }
        UserAccount account = new UserAccount(username, email, passwordEncoder.encode(rawPassword));
        return users.save(account);
    }

    public static class SignupException extends RuntimeException {
        public SignupException(String message) { super(message); }
    }
}
