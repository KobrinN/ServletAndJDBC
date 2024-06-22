package org.example.map.mapper;

import org.example.map.dto.RoleDto;
import org.example.map.request.newRequest.NewRoleRequest;
import org.example.map.request.updateRequest.UpdateRoleRequest;
import org.example.model.Role;
import org.mapstruct.Mapper;

@Mapper
public interface RoleMapper {
    RoleDto toDto(Role role);
    Role toModel(RoleDto roleDto);

    NewRoleRequest toNewRequest(Role role);
    Role toModel(NewRoleRequest request);

    UpdateRoleRequest toUpdateRequest(Role role);
    Role toModel(UpdateRoleRequest request);
}
