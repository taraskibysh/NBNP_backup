package com.nbnp.trip_to_go.auth;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.exception.FieldNotUniqueException;
import com.nbnp.trip_to_go.exception.PasswordMismatchException;
import com.nbnp.trip_to_go.exception.UserNotFoundException;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Role;
import com.nbnp.trip_to_go.repository.AppUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AppUserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AppUserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        if (!request.password().equals(request.passwordConfirmation())) {
            throw new PasswordMismatchException("Passwords do not match");
        }
        if (repository.existsByEmail(request.email())) {
            throw new FieldNotUniqueException("email", request.email());
        }
        AppUser user = new AppUser();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setUserPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);

        repository.save(user);
        String jwtToken = jwtService.generateToken(user, user.getId());

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        AppUser user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        String jwtToken = jwtService.generateToken(user, user.getId());
        return new AuthenticationResponse(jwtToken);
    }
}
