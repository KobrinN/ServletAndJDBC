package org.example.repo.impl;

import org.example.db.ConnectionManagerImpl;
import org.example.model.Product;
import org.example.model.Role;
import org.example.model.User;
import org.example.repo.ProductRepo;
import org.example.repo.UserRepo;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepoImplTest {
    private static final String INIT_SQL = "sql/schema_and_data.sql";
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript(INIT_SQL);
    private static final JdbcDatabaseDelegate delegate = new JdbcDatabaseDelegate(container, "");
    private static UserRepo userRepo;
    private static ProductRepo productRepo;

    @BeforeAll
    static void beforeAll() throws SQLException {
        container.start();
        ConnectionManagerImpl.setTestConnection(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        userRepo = UserRepoImpl.getInstance();
        productRepo = ProductRepoImpl.getInstance();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(delegate, INIT_SQL);
    }

    @Test
    void save() {
        String name = "product";
        Long quantity = 10L;
        Long numberOfSold = 5L;
        Double price = 100.0;
        Product product = new Product(null, name, quantity, numberOfSold, price);

        product = productRepo.save(product);

        Assertions.assertEquals(1L, product.getId());
        Assertions.assertEquals(name, product.getName());
        Assertions.assertEquals(quantity, product.getQuantity());
        Assertions.assertEquals(numberOfSold, product.getNumberOfSold());
        Assertions.assertEquals(price, product.getPrice());
    }

    @Test
    void update() {
        String name1 = "product1";
        String name2 = "product2";
        Long quantity = 10L;
        Long numberOfSold = 5L;
        Double price = 100.0;
        Product product = new Product(null, name1, quantity, numberOfSold, price);
        product = productRepo.save(product);

        product.setName(name2);
        productRepo.update(product);

        Assertions.assertEquals(1L, product.getId());
        Assertions.assertEquals(name2, product.getName());
    }

    @Test
    void deleteById() {
        String name= "product";
        Long quantity = 10L;
        Long numberOfSold = 5L;
        Double price = 100.0;
        Product product = new Product(null, name, quantity, numberOfSold, price);
        productRepo.save(product);

        productRepo.deleteById(1L);
        boolean existCheck = productRepo.exitsById(1L);

        Assertions.assertFalse(existCheck);
    }

    @Test
    void findById() {
        String name= "product";
        Long quantity = 10L;
        Long numberOfSold = 5L;
        Double price = 100.0;
        Product product = new Product(null, name, quantity, numberOfSold, price);
        productRepo.save(product);

        var productFounded = productRepo.findById(1L);

        Assertions.assertEquals(product.getName(), productFounded.get().getName());
        Assertions.assertEquals(product.getQuantity(), productFounded.get().getQuantity());
        Assertions.assertEquals(product.getNumberOfSold(), productFounded.get().getNumberOfSold());
        Assertions.assertEquals(product.getPrice(), productFounded.get().getPrice());
    }

    @Test
    void findByUserId() {
        String name1 = "product1";
        String name2 = "product2";
        Long quantity1 = 10L;
        Long quantity2 = 20L;
        Long numberOfSold1 = 5L;
        Long numberOfSold2 = 10L;
        Double price1 = 100.0;
        Double price2 = 200.0;
        String firstname = "Oleg";
        String lastname = "Olegovich";
        Role role = new Role(1L, "ROLE_USER");
        User user = new User(null, firstname, lastname, role);
        user = userRepo.save(user);
        Product product1 = new Product(null, name1, quantity1, numberOfSold1, price1, List.of(user));
        Product product2 = new Product(null, name2, quantity2, numberOfSold2, price2, List.of(user));
        product1 = productRepo.save(product1);
        product2 = productRepo.save(product2);

        List<Product> products = productRepo.findByUserId(1L);

        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals(product1.getId(), products.get(0).getId());
        Assertions.assertEquals(product2.getId(), products.get(1).getId());
    }

    @Test
    void findAll() {
        String name1 = "product1";
        String name2 = "product2";
        Long quantity1 = 10L;
        Long quantity2 = 20L;
        Long numberOfSold1 = 5L;
        Long numberOfSold2 = 10L;
        Double price1 = 100.0;
        Double price2 = 200.0;
        Product product1 = new Product(null, name1, quantity1, numberOfSold1, price1);
        Product product2 = new Product(null, name2, quantity2, numberOfSold2, price2);
        product1 = productRepo.save(product1);
        product2 = productRepo.save(product2);

        List<Product> products = productRepo.findAll();

        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals(product1.getId(), products.get(0).getId());
        Assertions.assertEquals(product2.getId(), products.get(1).getId());
    }

    @Test
    void exitsById() {
        String name= "product";
        Long quantity = 10L;
        Long numberOfSold = 5L;
        Double price = 100.0;
        Product product = new Product(null, name, quantity, numberOfSold, price);
        productRepo.save(product);

        Assertions.assertTrue(productRepo.exitsById(1L));
        Assertions.assertFalse(productRepo.exitsById(2L));
    }
}