package ru.viktorgezz.JWTSecuritySpring.dto.rq;

public class RegisterRequestDto extends AccountDataAbstract{

    public RegisterRequestDto(String login, String password) {
        super(login, password);
    }
}
