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

public class User {
    private final UserRepo userRepo = UserRepoImpl.getInstance();
    private final RoleRepo roleRepo = RoleRepoImpl.getInstance();
    private final ProductRepo productRepo = ProductRepoImpl.getInstance();
    private Long id;
    private String firstName;
    private String lastName;
    private Role role;
    private List<Product> products;

    public User() {}

    public User(Long id, String firstName, String lastName, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public User(Long id, String firstName, String lastName, Role role, List<Product> products) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Role getRole() {
        if (role == null && id != null) role = roleRepo.findById(userRepo.findById(id).get().getId()).get();
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public List<Product> getProducts() {
        if (products == null && id != null) products = productRepo.findByUserId(id);
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(role, user.role) && Objects.equals(products, user.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, role, products);
    }
}
