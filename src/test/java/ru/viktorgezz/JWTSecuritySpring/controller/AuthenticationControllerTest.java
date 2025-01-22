package ru.viktorgezz.JWTSecuritySpring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.LoginAccountDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.RegisterRequestDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.AuthenticationResponse;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.RegisterResponseDto;
import ru.viktorgezz.JWTSecuritySpring.exception.CommonProjectException;
import ru.viktorgezz.JWTSecuritySpring.exception.GlobalExceptionHandler;
import ru.viktorgezz.JWTSecuritySpring.service.implementations.AccountService;
import ru.viktorgezz.JWTSecuritySpring.service.implementations.AuthenticationService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .setControllerAdvice(globalExceptionHandler)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void signUp_whenValidRequest_thenReturnsOk() throws Exception {
        RegisterRequestDto registerRequestDto =
                new RegisterRequestDto("Ryan_Gosling", "password123");

        final long testId = 1L;
        when(authenticationService.saveAccount(any(RegisterRequestDto.class)))
                .thenReturn(Optional.of(new RegisterResponseDto(testId, "Ryan_Gosling")));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("Ryan_Gosling"));
    }

    @Test
    void signUp_whenUserAlreadyExists_thenThrowsCustomException() throws Exception {
        RegisterRequestDto existUser =
                new RegisterRequestDto("existUser", "dont know password");

        when(authenticationService.saveAccount(any(RegisterRequestDto.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(existUser)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.error").value("Пользователь с таким именем уже существует"));
    }

    @Test
    void signUp_whenLoginSizeLessTwoChar_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto("R", "random_password");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value("Имя пользователя должно содержать от 2 до 50 символов"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenLoginSizeEqualsNull_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto(null, "random_password");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value("Имя должно быть заполнено"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenLoginHasLeadingSpace_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto(" Ryan_Gosling", "validPassword123");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value("Имя пользователя не должно содержать пробелов в начале или конце"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenLoginHasTrailingSpace_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto("Ryan_Gosling ", "validPassword123");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value("Имя пользователя не должно содержать пробелов в начале или конце"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void signUp_whenPasswordSizeMore255Char_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto("Ryan_Gosling", "A".repeat(256));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password").value("Длина пароля должна быть не более 255 символов"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenPasswordSizeEqualsNull_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto("Ryan_Gosling", null);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password").value("Пароль должен быть заполнен"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenPasswordHasLeadingSpace_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto("Ryan_Gosling", " password123");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password").value("Пароль не должен содержать пробелов в начале или конце"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUp_whenPasswordHasTrailingSpace_thenReturnBadRequest() throws Exception {
        RegisterRequestDto invalidRequest = new RegisterRequestDto("Ryan_Gosling", "password123 ");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.password").value("Пароль не должен содержать пробелов в начале или конце"))
                .andExpect(status().isBadRequest());
    }

    ////////////////
    @Test
    void signIn_whenValidRequest_thenReturnsOk() throws Exception {
        LoginAccountDto loginAccountDto = new LoginAccountDto("Ryan_Gosling", "password123");

        final long testExpiresIn = 2113L;
        when(authenticationService.authenticate(any(LoginAccountDto.class)))
                .thenReturn(Optional.of(new AuthenticationResponse("token", testExpiresIn)));

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginAccountDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void signIn_whenAccountNotFound_thenReturnsNotFound() throws Exception {
        LoginAccountDto loginAccountDto = new LoginAccountDto("Ryan_Gosling", "password123");

        when(authenticationService.authenticate(any(LoginAccountDto.class)))
                .thenThrow(new CommonProjectException("Аккаунт не найден", HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginAccountDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Аккаунт не найден"));
    }

    @Test
    void signIn_whenAccountDisable_thenReturnsForbidden() throws Exception {
        LoginAccountDto loginAccountDto = new LoginAccountDto("Ryan_Gosling", "password123");

        when(authenticationService.authenticate(any(LoginAccountDto.class)))
                .thenThrow(new CommonProjectException("Аккаунт не активирован", HttpStatus.FORBIDDEN));

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginAccountDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Аккаунт не активирован"));
    }
}
