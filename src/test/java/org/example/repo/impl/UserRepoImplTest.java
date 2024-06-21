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
import java.util.Optional;

class UserRepoImplTest {
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
        String firstName = "Mark";
        String lastName = "Markovich";
        Role role = new Role(1L, "USER_ROLE");
        User user = new User(
                null,
                firstName,
                lastName,
                role);

        user = userRepo.save(user);
        Optional<User> userResult = userRepo.findById(1L);

        Assertions.assertTrue(userResult.isPresent());
        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals(firstName, userResult.get().getFirstName());
        Assertions.assertEquals(lastName, userResult.get().getLastName());
        Assertions.assertEquals(role, userResult.get().getRole());
    }

    @Test
    void update() {
        String firstName1 = "Oleg";
        String firstName2 = "Aleksey";
        String lastname1 = "Olegovich";
        String lastName2 = "Alekseyevich";
        Role role1 = new Role(1L, "USER_ROLE");
        Role role2 = new Role(2L, "ADMIN_ROLE");
        User user1 = new User(null, firstName1, lastname1, role1);
        userRepo.save(user1);

        User user2 = new User(1L, firstName2, lastName2, role2);
        userRepo.update(user2);
        User userUpdate = userRepo.findById(1L).get();

        Assertions.assertEquals(user2, userUpdate);
    }

    @Test
    void deleteById() {
        String firstName1 = "Oleg";
        String firstName2 = "Aleksey";
        String lastname1 = "Olegovich";
        String lastName2 = "Alekseyevich";
        Role role1 = new Role(1L, "USER_ROLE");
        Role role2 = new Role(2L, "ADMIN_ROLE");
        User user1 = new User(null, firstName1, lastname1, role1);
        User user2 = new User(null, firstName2, lastName2, role2);
        userRepo.save(user1);
        userRepo.save(user2);

        userRepo.deleteById(1L);

        Assertions.assertEquals(1, userRepo.findAll().size());
    }

    @Test
    void findById() {
        String firstName1 = "Oleg";
        String lastname1 = "Olegovich";
        Role role1 = new Role(1L, "USER_ROLE");
        User user1 = new User(null, firstName1, lastname1, role1);
        user1 = userRepo.save(user1);

        Optional<User> foundUser = userRepo.findById(1L);
        user1.setId(1L);
        user1.setProducts(List.of());
        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(user1, foundUser.get());
    }

    @Test
    void findByProductId() {
        String firstName1 = "Oleg";
        String firstName2 = "Aleksey";
        String lastname1 = "Olegovich";
        String lastName2 = "Alekseyevich";
        Role role1 = new Role(1L, "USER_ROLE");
        Role role2 = new Role(2L, "ADMIN_ROLE");
        Product product = new Product(null, "product", 0L, 0L, 1.1);
        product = productRepo.save(product);
        List<Product> products = List.of(product);
        User user1 = new User(null, firstName1, lastname1, role1, products);
        User user2 = new User(null, firstName2, lastName2, role2, products);
        user1 = userRepo.save(user1);
        user2 = userRepo.save(user2);

        List<User> users = userRepo.findByProductId(product.getId());

        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(user1.getId(), users.get(0).getId());
        Assertions.assertEquals(user2.getId(), users.get(1).getId());
    }

    @Test
    void findByRoleId() {
        String firstName1 = "Oleg";
        String firstName2 = "Aleksey";
        String lastname1 = "Olegovich";
        String lastName2 = "Alekseyevich";
        Role role1 = new Role(1L, "USER_ROLE");
        Role role2 = new Role(2L, "ADMIN_ROLE");
        User user1 = new User(null, firstName1, lastname1, role1);
        User user2 = new User(null, firstName2, lastName2, role2);
        user1 = userRepo.save(user1);
        user2 = userRepo.save(user2);

        var userFounded1 = userRepo.findByRoleId(1L);
        var userFounded2 = userRepo.findByRoleId(2L);

        Assertions.assertEquals(1, userFounded1.size());
        Assertions.assertEquals(1, userFounded2.size());
        Assertions.assertEquals(user1.getId(), userFounded1.get(0).getId());
        Assertions.assertEquals(user2.getId(), userFounded2.get(0).getId());
    }

    @Test
    void findAll() {
        String firstName1 = "Oleg";
        String firstName2 = "Aleksey";
        String lastname1 = "Olegovich";
        String lastName2 = "Alekseyevich";
        Role role1 = new Role(1L, "USER_ROLE");
        Role role2 = new Role(2L, "ADMIN_ROLE");
        User user1 = new User(null, firstName1, lastname1, role1);
        User user2 = new User(null, firstName2, lastName2, role2);
        user1 = userRepo.save(user1);
        user2 = userRepo.save(user2);

        var userFounded = userRepo.findAll();

        Assertions.assertEquals(2, userFounded.size());
        Assertions.assertEquals(user1.getId(), userFounded.get(0).getId());
        Assertions.assertEquals(user2.getId(), userFounded.get(1).getId());
    }

    @Test
    void exitsById() {
        String firstName = "Oleg";
        String lastname = "Olegovich";
        Role role = new Role(1L, "USER_ROLE");
        User user = new User(null, firstName, lastname, role);
        user = userRepo.save(user);

        boolean saveCheck1 = userRepo.exitsById(1L);
        boolean saveCheck2 = userRepo.exitsById(2L);

        Assertions.assertTrue(saveCheck1);
        Assertions.assertFalse(saveCheck2);

    }
}