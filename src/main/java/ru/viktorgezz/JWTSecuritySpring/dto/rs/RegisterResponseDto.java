package ru.viktorgezz.JWTSecuritySpring.dto.rs;

import ru.viktorgezz.JWTSecuritySpring.util.Role;

public class RegisterResponseDto {

    private Long id;

    private String login;

    private Role role;

    public RegisterResponseDto(Long id, String login, Role role) {
        this.id = id;
        this.login = login;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
