package ru.viktorgezz.JWTSecuritySpring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.viktorgezz.JWTSecuritySpring.dto.rs.RegisterResponseDto;
import ru.viktorgezz.JWTSecuritySpring.model.Account;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    RegisterResponseDto toDto(Account account);
}
