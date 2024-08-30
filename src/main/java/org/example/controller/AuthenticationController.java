package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.example.dto.auth.AuthenticateRequest;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.UserDto;
import org.example.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticateRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterRequest request) throws BadRequestException {
        return authenticationService.register(request);
    }
}
