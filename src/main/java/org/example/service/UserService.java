package org.example.service;

import org.example.map.dto.UserDto;
import org.example.map.request.newRequest.NewUserRequest;
import org.example.map.request.updateRequest.UpdateUserRequest;

import java.util.List;

public interface UserService {
    Long create(NewUserRequest request);
    List<UserDto> readAll();
    UserDto readById(Long id);
    UserDto update(UpdateUserRequest request);
    void delete(Long id);
}
