package org.example.model;

import org.example.repo.ProductRepo;
import org.example.repo.RoleRepo;
import org.example.repo.UserRepo;
import org.example.repo.UserToProductRepo;
import org.example.repo.impl.ProductRepoImpl;
import org.example.repo.impl.RoleRepoImpl;
import org.example.repo.impl.UserRepoImpl;
import org.example.repo.impl.UserToProductRepoImpl;

import java.util.List;
import java.util.Objects;

public class Role {
    private final UserRepo userRepo = UserRepoImpl.getInstance();
    private Long id;
    private String name;
    private List<User> users;

    public Role() {}

    public Role(Long id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        if(users == null) users = userRepo.findByRoleId(id);
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) && Objects.equals(name, role.name) && Objects.equals(users, role.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, users);
    }
}
