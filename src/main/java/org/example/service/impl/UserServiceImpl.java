package org.example.service.impl;

import org.example.exception.BadRequestException;
import org.example.map.dto.UserDto;
import org.example.map.mapper.RoleMapper;
import org.example.map.mapper.RoleMapperImpl;
import org.example.map.mapper.UserMapper;
import org.example.map.mapper.UserMapperImpl;
import org.example.map.request.newRequest.NewUserRequest;
import org.example.map.request.updateRequest.UpdateUserRequest;
import org.example.model.User;
import org.example.repo.RoleRepo;
import org.example.repo.UserRepo;
import org.example.repo.impl.RoleRepoImpl;
import org.example.repo.impl.UserRepoImpl;
import org.example.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserMapper userMapper = new UserMapperImpl();

    public UserServiceImpl() {
        userRepo = UserRepoImpl.getInstance();
        roleRepo = RoleRepoImpl.getInstance();
    }

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public Long create(NewUserRequest request) {
        if(!checkNewRequest(request)) throw new BadRequestException("Invalid fields!");

        User user = userMapper.toModel(request);
        user.setRole(roleRepo.findById(1L).get());
        user = userRepo.save(user);

        return user.getId();
    }

    @Override
    public List<UserDto> readAll() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserDto read(Long id) {
        if(!userRepo.exitsById(id)) throw new BadRequestException("Invalid ID!");
        return userMapper.toDto(userRepo.findById(id).get());
    }

    @Override
    public UserDto update(UpdateUserRequest request) {
        if(!checkUpdateRequest(request)) throw new BadRequestException("Invalid Fields!");
        User user = userMapper.toModel(request);
        if(userRepo.exitsById(user.getId())){
            userRepo.update(user);
        }
        else user = userRepo.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    private boolean checkNewRequest(NewUserRequest request){
        return (!request.getFirstName().isEmpty() &&
                !request.getLastName().isEmpty());
    }

    private boolean checkUpdateRequest(UpdateUserRequest request){
        return ((request.getId() != null) &&
                !request.getFirstName().isEmpty() &&
                !request.getLastName().isEmpty() &&
                (request.getRole() != null));
    }
}
