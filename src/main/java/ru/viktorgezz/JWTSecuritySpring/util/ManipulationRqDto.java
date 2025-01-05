package ru.viktorgezz.JWTSecuritySpring.util;

import ru.viktorgezz.JWTSecuritySpring.dto.rq.RegisterRequestDto;

public class ManipulationRqDto {

    public static void removeSpacesBeginAndEnd(RegisterRequestDto registerDto) {
        registerDto.setLogin(registerDto.getLogin().strip());
    }

}
