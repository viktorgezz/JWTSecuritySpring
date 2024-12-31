package ru.viktorgezz.JWTSecuritySpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.viktorgezz.JWTSecuritySpring.exception.CustomException;
import ru.viktorgezz.JWTSecuritySpring.model.Account;
import ru.viktorgezz.JWTSecuritySpring.service.implementations.AccountService;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(accountService.getAll());
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> findAccount(@PathVariable Long id) {
        return ResponseEntity
                .ok(accountService
                        .findAccountById(id)
                        .orElseThrow(
                                () -> new CustomException("Аккаунт не найден", HttpStatus.BAD_REQUEST)));
    }
}
