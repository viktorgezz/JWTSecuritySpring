package ru.viktorgezz.JWTSecuritySpring.dto.rq;

public class LoginAccountDto extends AccountDataAbstract {

    public LoginAccountDto(String login, String password) {
        super(login, password);
    }
}
