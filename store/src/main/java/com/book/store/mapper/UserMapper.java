package com.book.store.mapper;

import com.book.store.dto.CreateUserRequestDto;
import com.book.store.dto.CreateUserResponseDto;
import com.book.store.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toUser(CreateUserRequestDto createUserRequestDto);

    CreateUserResponseDto toCreateUserResponse(User user);
}

