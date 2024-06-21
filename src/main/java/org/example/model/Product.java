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

public class Product {
    private final UserRepo userRepo = UserRepoImpl.getInstance();
    private Long id;
    private String name;
    private Long quantity;
    private Long numberOfSold;
    private Double price;
    private List<User> users;

    public Product() {}

    public Product(Long id, String name, Long quantity, Long numberOfSold, Double price, List<User> users) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.numberOfSold = numberOfSold;
        this.price = price;
        this.users = users;
    }

    public Product(Long id, String name, Long quantity, Long numberOfSold, Double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.numberOfSold = numberOfSold;
        this.price = price;
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getNumberOfSold() {
        return numberOfSold;
    }

    public void setNumberOfSold(Long numberOfSold) {
        this.numberOfSold = numberOfSold;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<User> getUsers() {
        if(users == null && id != null) users = userRepo.findByProductId(id);
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(quantity, product.quantity) && Objects.equals(numberOfSold, product.numberOfSold) && Objects.equals(price, product.price) && Objects.equals(users, product.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, numberOfSold, price, users);
    }
}
