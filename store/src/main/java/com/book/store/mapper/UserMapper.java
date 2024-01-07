package com.book.store.mapper;

import com.book.store.dto.CreateUserRequestDto;
import com.book.store.dto.CreateUserResponseDto;
import com.book.store.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toUser(CreateUserRequestDto createUserRequestDto);

    CreateUserResponseDto toCreateUserResponse(User user);
}


