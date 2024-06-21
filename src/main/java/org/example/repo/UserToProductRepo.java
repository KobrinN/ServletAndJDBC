package org.example.repo;

import org.example.model.Product;
import org.example.model.User;
import org.example.model.UserToProduct;

import java.util.List;

public interface UserToProductRepo extends Repository<UserToProduct, Long> {
    boolean deleteByUserId(Long id);
    boolean deleteByProductId(Long id);
    List<Product> findByUserId(Long id);
    List<User> findByProductId(Long id);
    boolean exitsByUserId(Long id);
    boolean exitsByProductId(Long id);

}
