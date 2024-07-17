package org.example.service.impl;

import org.example.exception.BadRequestException;
import org.example.map.dto.RoleResponse;
import org.example.map.mapper.RoleMapper;
import org.example.map.mapper.RoleMapperImpl;
import org.example.map.request.RoleRequest;
import org.example.model.Role;
import org.example.repo.RoleRepo;
import org.example.repo.impl.RoleRepoImpl;
import org.example.service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    private final RoleMapper roleMapper = new RoleMapperImpl();

    public RoleServiceImpl() {
        roleRepo = RoleRepoImpl.getInstance();
    }

    public RoleServiceImpl(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public Long post(RoleRequest request) {
        if (checkNewRequest(request)) throw new BadRequestException("Invalid fields!");
        Role role = roleMapper.toModel(request);
        role = roleRepo.save(role);
        return role.getId();
    }

    @Override
    public RoleResponse get(Long id) {
        if (!roleRepo.exitsById(id)) throw new BadRequestException("Not valid ID!");
        return roleMapper.toResponse(roleRepo.findById(id).get());
    }

    @Override
    public List<RoleResponse> getAll() {
        return roleRepo.findAll().stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Override
    public RoleResponse edit(Long id, RoleRequest request) {
        if (!checkUpdateRequest(request)) throw new BadRequestException("Invalid fields!");
        Role role = roleMapper.toModel(request);
        if (roleRepo.exitsById(role.getId())) {
            roleRepo.update(role);
        } else role = roleRepo.save(role);
        return roleMapper.toResponse(role);
    }

    @Override
    public void delete(Long id) {
        roleRepo.deleteById(id);
    }

    private boolean checkNewRequest(RoleRequest request) {
        return (!request.getName().isEmpty());
    }

    private boolean checkUpdateRequest(RoleRequest request) {
        return (!request.getName().isEmpty());
    }
}
