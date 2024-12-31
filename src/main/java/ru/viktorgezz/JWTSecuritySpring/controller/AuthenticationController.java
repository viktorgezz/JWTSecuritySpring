package ru.viktorgezz.JWTSecuritySpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.RegisterResponseDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.AuthenticationResponse;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.LoginAccountDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.RegisterRequestDto;
import ru.viktorgezz.JWTSecuritySpring.service.implementations.AuthenticationService;
import ru.viktorgezz.JWTSecuritySpring.exception.CustomException;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity
                .ok(authenticationService
                        .signup(registerRequestDto)
                        .orElseThrow(
                                () -> new CustomException("Ошибка регистрации", HttpStatus.BAD_REQUEST)));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginAccountDto loginAccountDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginAccountDto));
    }
}
