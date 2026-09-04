package com.ems.mapper;

import com.ems.dto.LoginDto;
import com.ems.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    LoginDto toDto(User user);
}