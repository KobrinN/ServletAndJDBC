package org.example.repo;

import org.example.model.Product;
import org.example.model.User;

import java.util.List;

public interface UserRepo extends Repository<User, Long>{
    public List<User> findByProductId(Long id);

    public List<User> findByRoleId(Long id);
}
