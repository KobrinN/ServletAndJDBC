package org.example.map.mapper;

import org.example.map.dto.UserDto;
import org.example.map.request.newRequest.NewUserRequest;
import org.example.map.request.updateRequest.UpdateUserRequest;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(target = "role", source = "user.role.name")
    UserDto toDto(User user);
    @Mapping(source = "role", target = "role.name")
    User toModel(UserDto userDto);

    NewUserRequest toNewRequest(User user);
    User toModel(NewUserRequest request);

    @Mapping(target = "role", source = "user.role.id")
    UpdateUserRequest toUpdateRequest(User user);
    @Mapping(target = "role.id", source = "role")
    User toModel(UpdateUserRequest request);
}
