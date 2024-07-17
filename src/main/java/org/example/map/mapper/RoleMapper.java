package org.example.map.mapper;

import org.example.map.dto.RoleResponse;
import org.example.map.request.RoleRequest;
import org.example.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleResponse toResponse(Role role);

    Role toModel(RoleRequest request);
}
