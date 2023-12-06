package com.book.store.mapper;

import com.book.store.dto.CreateUserRequest;
import com.book.store.dto.CreateUserResponse;
import com.book.store.model.Role;
import com.book.store.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    User toUser(CreateUserRequest createUserRequest);

    CreateUserResponse toCreateUserResponse(User user);
}

