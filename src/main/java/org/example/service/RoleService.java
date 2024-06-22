package org.example.service;

import org.example.map.dto.RoleDto;
import org.example.map.request.newRequest.NewRoleRequest;
import org.example.map.request.updateRequest.UpdateRoleRequest;

import java.util.List;

public interface RoleService {
    Long create(NewRoleRequest request);
    RoleDto read(Long id);
    List<RoleDto> readAll();
    RoleDto update(UpdateRoleRequest request);
    void delete(Long id);
}
