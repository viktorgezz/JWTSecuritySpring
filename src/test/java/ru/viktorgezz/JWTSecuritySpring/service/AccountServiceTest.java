package ru.viktorgezz.JWTSecuritySpring.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.viktorgezz.JWTSecuritySpring.model.Account;
import ru.viktorgezz.JWTSecuritySpring.repo.AccountRepository;
import ru.viktorgezz.JWTSecuritySpring.service.implementations.AccountService;
import ru.viktorgezz.JWTSecuritySpring.util.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAccountByLogin() {
        String login = "login";
        Account account = new Account("login", "password", Role.ROLE_USER);

        when(accountRepository.findAccountByLogin(anyString())).thenReturn(Optional.of(account));

        // Act
        Optional<Account> result = accountService.findAccountByLogin(login);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(login, result.get().getLogin());
        verify(accountRepository, times(1)).findAccountByLogin(login);
    }

    @Test
    void testNotFindAccountByLogin() {
        String login = "login";

        when(accountRepository.findAccountByLogin(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Account> result = accountService.findAccountByLogin(login);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveAccount() {
        // Arrange
        Account account = new Account("testuser", "password", Role.ROLE_USER);
        when(accountRepository.save(account)).thenReturn(account);

        // Act
        Account result = accountService.save(account);

        // Assert
        assertNotNull(result);
        assertEquals(account.getLogin(), result.getLogin());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testGetAllAccounts() {
        // Arrange
        List<Account> accounts = Arrays.asList(
                new Account("user1", "password1", Role.ROLE_USER),
                new Account("user2", "password2", Role.ROLE_ADMIN)
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        // Act
        List<Account> result = accountService.getAll();

        // Assert
        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void testFindAccountById() {
        // Arrange
        Long id = 1L;
        Account account = new Account("testuser", "password", Role.ROLE_USER);
        account.setId(id);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        // Act
        Optional<Account> result = accountService.findAccountById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
        verify(accountRepository, times(1)).findById(id);
    }

}
