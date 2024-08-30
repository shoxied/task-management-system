package org.example.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.dto.auth.AuthenticateRequest;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.UserDto;
import org.example.entity.User;
import org.example.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> authenticate(AuthenticateRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }

    public UserDto register(RegisterRequest request) throws BadRequestException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new BadRequestException("the user with the specified name already exists");
        }
        User user = userService.register(request);
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
