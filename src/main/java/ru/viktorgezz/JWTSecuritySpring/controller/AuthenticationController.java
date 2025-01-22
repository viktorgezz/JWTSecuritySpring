package ru.viktorgezz.JWTSecuritySpring.controller;

import jakarta.validation.Valid;
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
import ru.viktorgezz.JWTSecuritySpring.exception.CommonProjectException;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponseDto> signUp(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity
                .ok(authenticationService
                        .saveAccount(registerRequestDto)
                        .orElseThrow(
                                () -> new CommonProjectException(
                                        "Пользователь с таким именем уже существует", HttpStatus.BAD_REQUEST
                                )
                        )
                );
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signIn(@Valid @RequestBody LoginAccountDto loginAccountDto) {
        return ResponseEntity
                .ok(authenticationService
                        .authenticate(loginAccountDto)
                        .orElseThrow(
                                () -> new CommonProjectException(
                                        "Пользователь с таким именем не найден", HttpStatus.NOT_FOUND
                                )
                        )
                );
    }
}
