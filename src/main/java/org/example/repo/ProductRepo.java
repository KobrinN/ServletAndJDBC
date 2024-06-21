package org.example.repo;

import org.example.model.Product;

import java.util.List;

public interface ProductRepo extends Repository<Product, Long>{
    public List<Product> findByUserId(Long id);
}
