package ru.viktorgezz.JWTSecuritySpring.dto.rq;

import jakarta.validation.constraints.Size;

public class LoginAccountDto {

    @Size(min = 2, max = 50, message = "Имя пользователя должно содержать от 2 до 50 символов")
    private String login;

    @Size(max = 255, message = "Длина пароля должна быть до 255 символов")
    private String password;

    public LoginAccountDto() {
    }

    public LoginAccountDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
