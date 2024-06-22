package org.example.service.impl;

import org.example.exception.BadRequestException;
import org.example.map.dto.RoleDto;
import org.example.map.mapper.RoleMapper;
import org.example.map.mapper.RoleMapperImpl;
import org.example.map.request.newRequest.NewRoleRequest;
import org.example.map.request.updateRequest.UpdateRoleRequest;
import org.example.model.Role;
import org.example.repo.RoleRepo;
import org.example.repo.UserRepo;
import org.example.repo.impl.RoleRepoImpl;
import org.example.repo.impl.UserRepoImpl;
import org.example.service.RoleService;
import org.example.service.UserService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final RoleMapper roleMapper = new RoleMapperImpl();

    public RoleServiceImpl(){
        roleRepo = RoleRepoImpl.getInstance();
        userRepo = UserRepoImpl.getInstance();
    }
    public RoleServiceImpl(RoleRepo roleRepo, UserRepo userRepo){
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }
    @Override
    public Long create(NewRoleRequest request) {
        if(checkNewRequest(request)) throw new BadRequestException("Invalid fields!");
        Role role = roleMapper.toModel(request);
        role = roleRepo.save(role);
        return role.getId();
    }

    @Override
    public RoleDto read(Long id) {
        if(!roleRepo.exitsById(id)) throw new BadRequestException("Not valid ID!");
        return roleMapper.toDto(roleRepo.findById(id).get());
    }

    @Override
    public List<RoleDto> readAll() {
        return roleRepo.findAll().stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    public RoleDto update(UpdateRoleRequest request) {
        if(!checkUpdateRequest(request)) throw new BadRequestException("Invalid fields!");
        Role role = roleMapper.toModel(request);
        if(roleRepo.exitsById(role.getId())){
            roleRepo.update(role);
        }
        else role = roleRepo.save(role);
        return roleMapper.toDto(role);
    }

    @Override
    public void delete(Long id) {
        roleRepo.deleteById(id);
    }

    private boolean checkNewRequest(NewRoleRequest request){
        return (!request.getName().isEmpty());
    }

    private boolean checkUpdateRequest(UpdateRoleRequest request){
        return (!request.getName().isEmpty() &&
                request.getId() != null);
    }
}
