package ru.viktorgezz.JWTSecuritySpring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.AuthenticationResponse;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.RegisterResponseDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.LoginAccountDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.RegisterRequestDto;
import ru.viktorgezz.JWTSecuritySpring.exception.CommonProjectException;
import ru.viktorgezz.JWTSecuritySpring.model.Account;
import ru.viktorgezz.JWTSecuritySpring.service.implementations.AccountService;
import ru.viktorgezz.JWTSecuritySpring.service.implementations.AuthenticationService;
import ru.viktorgezz.JWTSecuritySpring.service.interfaces.JwtService;
import ru.viktorgezz.JWTSecuritySpring.util.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequestDto registerRequestDto;
    private LoginAccountDto loginAccountDto;
    private Account account;

    @BeforeEach
    void setUp() {
        registerRequestDto = new RegisterRequestDto("Ryan_Gosling", "testPassword");
        loginAccountDto = new LoginAccountDto("Ryan_Gosling", "testPassword");
        account = new Account("Ryan_Gosling", "encodedPassword", Role.ROLE_USER);
    }

    @Test
    void saveAccount_WhenAccountDoesNotExist_ShouldReturnRegisterResponseDto() {
        // Arrange
        when(accountService.findAccountByLogin(registerRequestDto.getLogin())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerRequestDto.getPassword())).thenReturn("encodedPassword");
        when(accountService.save(any(Account.class))).thenReturn(account);

        // Act
        Optional<RegisterResponseDto> savedAccountOpt = authenticationService.saveAccount(registerRequestDto);

        // Assert
        assertTrue(savedAccountOpt.isPresent());
        assertEquals(account.getLogin(), savedAccountOpt.get().getLogin());
        verify(accountService, times(1)).findAccountByLogin(registerRequestDto.getLogin());
        verify(accountService, times(1)).save(any(Account.class));
    }

    @Test
    void saveAccount_WhenAccountExists_ShouldReturnEmptyOptional() {
        // Arrange
        when(accountService.findAccountByLogin(registerRequestDto.getLogin())).thenReturn(Optional.of(account));

        // Act
        Optional<RegisterResponseDto> savedAccountOpt = authenticationService.saveAccount(registerRequestDto);

        // Assert
        assertFalse(savedAccountOpt.isPresent());
        verify(accountService, times(1)).findAccountByLogin(registerRequestDto.getLogin());
        verify(accountService, never()).save(any(Account.class));
    }

    @Test
    void authenticate_WhenAccountExistsAndEnabled_ShouldReturnAuthenticationResponse() throws CommonProjectException {
        // Arrange
        when(accountService.findAccountByLogin(loginAccountDto.getLogin())).thenReturn(Optional.of(account));
        when(jwtService.generateToken(account)).thenReturn("jwtToken");
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        // Act
        Optional<AuthenticationResponse> result = authenticationService.authenticate(loginAccountDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("jwtToken", result.get().getToken());
        assertEquals(3600L, result.get().getExpiresIn());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken(account);
    }

    @Test
    void authenticate_WhenAccountDoesNotExist_ShouldReturnEmptyOptional() throws CommonProjectException {
        // Arrange
        when(accountService.findAccountByLogin(loginAccountDto.getLogin())).thenReturn(Optional.empty());

        // Act
        Optional<AuthenticationResponse> result = authenticationService.authenticate(loginAccountDto);

        // Assert
        assertFalse(result.isPresent());
        verify(accountService, times(1)).findAccountByLogin(loginAccountDto.getLogin());
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    // Добавить тест проверки, активирован ли аккаунт, при добавление подтверждения с почты.
//    @Test
//    void authenticate_WhenAccountIsDisabled_ShouldThrowCommonProjectException() {
//        // Arrange
//        when(account.isEnabled()).thenReturn(false);
//        when(accountService.findAccountByLogin(loginAccountDto.getLogin())).thenReturn(Optional.of(account));
//
//        // Act & Assert
//        CommonProjectException exception = assertThrows(CommonProjectException.class, () -> {
//            authenticationService.authenticate(loginAccountDto);
//        });
//
//        assertEquals("Аккаунт не активирован", exception.getMessage());
//        verify(accountService, times(1)).findAccountByLogin(loginAccountDto.getLogin());
//        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
//    }
}
