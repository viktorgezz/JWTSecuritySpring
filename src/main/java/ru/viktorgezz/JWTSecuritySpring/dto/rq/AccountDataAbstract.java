package ru.viktorgezz.JWTSecuritySpring.dto.rq;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public abstract class AccountDataAbstract {

    @Size(min = 2, max = 50, message = "Имя пользователя должно содержать от 2 до 50 символов")
    @NotNull(message = "Имя должно быть заполнено")
    @Pattern(regexp = "^\\S+(?:\\s+\\S+)*$", message = "Имя пользователя не должно содержать пробелов в начале или конце")
    protected final String login;

    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    @NotNull(message = "Пароль должен быть заполнен")
    @Pattern(regexp = "^\\S+(?:\\s+\\S+)*$", message = "Пароль не должен содержать пробелов в начале или конце")
    protected final String password;

    public AccountDataAbstract(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
