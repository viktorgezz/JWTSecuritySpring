package ru.viktorgezz.JWTSecuritySpring.service.implementations;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.RegisterResponseDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.AuthenticationResponse;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.LoginAccountDto;
import ru.viktorgezz.JWTSecuritySpring.dto.rq.RegisterRequestDto;
import ru.viktorgezz.JWTSecuritySpring.mapper.AccountMapper;
import ru.viktorgezz.JWTSecuritySpring.model.Account;
import ru.viktorgezz.JWTSecuritySpring.service.interfaces.JwtService;
import ru.viktorgezz.JWTSecuritySpring.exception.CommonProjectException;
import ru.viktorgezz.JWTSecuritySpring.util.Role;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Autowired
    public AuthenticationService(AccountService accountService,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public Optional<RegisterResponseDto> saveAccount(RegisterRequestDto inputAccount) {
        if (accountService.findAccountByLogin(inputAccount.getLogin()).isPresent()) {
            return Optional.empty();
        }

        return Optional.of(
                AccountMapper.INSTANCE.toDto(
                        accountService.save(
                                new Account(
                                        inputAccount.getLogin(),
                                        passwordEncoder.encode(inputAccount.getPassword()),
                                        Role.ROLE_USER)
                        )
                )
        );
    }

    public Optional<AuthenticationResponse> authenticate(LoginAccountDto loginAccountDto)
            throws CommonProjectException {
        // получить аккаунт

        // валидация
        Optional<Account> accountOpt = accountService
                .findAccountByLogin(loginAccountDto.getLogin());

        if (accountOpt.isEmpty()) {
            return Optional.empty();
        }
        Account account = accountOpt.get();

        if (!account.isEnabled()) {
            throw new CommonProjectException("Аккаунт не активирован", HttpStatus.BAD_REQUEST);
        }
        // - валидация

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginAccountDto.getLogin(),
                loginAccountDto.getPassword()
        ));

        String jwtToken = jwtService.generateToken(account);

        return Optional.of(new AuthenticationResponse(jwtToken, jwtService.getExpirationTime()));
    }
}
