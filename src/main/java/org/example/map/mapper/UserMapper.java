package org.example.map.mapper;

import org.example.map.dto.UserResponse;
import org.example.map.request.UserRequest;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(target = "role", source = "user.role.name")
    UserResponse toResponse(User user);

    User toModel(UserRequest request);
}
