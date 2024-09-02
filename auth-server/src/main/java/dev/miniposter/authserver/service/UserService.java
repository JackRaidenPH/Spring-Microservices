package dev.miniposter.authserver.service;

import dev.miniposter.authserver.exception.EmailAlreadyExistsException;
import dev.miniposter.authserver.exception.UsernameAlreadyExistsException;
import dev.miniposter.authserver.model.User;
import dev.miniposter.authserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Level;

@Service
@Log
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public void create(User user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException(user.getEmail());
        }

        this.userRepository.save(user);
        log.log(Level.INFO, "Registered new user, email: " + user.getEmail());
    }

    public User getByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!")
                );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getByUsername(username);
    }
}