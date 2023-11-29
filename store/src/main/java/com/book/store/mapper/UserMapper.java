package com.book.store.mapper;

import com.book.store.dto.CreateUserRequest;
import com.book.store.dto.CreateUserResponse;
import com.book.store.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(CreateUserRequest createUserRequest);

    CreateUserResponse toCreateUserResponse(User user);
}

