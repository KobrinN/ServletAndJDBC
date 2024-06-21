package org.example.repo.impl;

import org.example.db.ConnectionManagerImpl;
import org.example.model.Role;
import org.example.repo.ProductRepo;
import org.example.repo.RoleRepo;
import org.example.repo.UserRepo;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

import java.sql.SQLException;
import java.util.AbstractSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleRepoImplTest {
    private static final String INIT_SQL = "sql/schema_and_data.sql";
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript(INIT_SQL);
    private static final JdbcDatabaseDelegate delegate = new JdbcDatabaseDelegate(container, "");
    private static RoleRepo roleRepo;

    @BeforeAll
    static void beforeAll() throws SQLException {
        container.start();
        ConnectionManagerImpl.setTestConnection(
                container.getJdbcUrl(),
                container.getUsername(),
                container.getPassword()
        );
        roleRepo = RoleRepoImpl.getInstance();
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
        String name = "role";
        Role role = new Role(null, name);

        roleRepo.save(role);

        Assertions.assertTrue(roleRepo.exitsById(3L));
    }

    @Test
    void update() {
        String name = "role";
        Role role = new Role(1L, name);

        roleRepo.update(role);
        role = roleRepo.findById(1L).get();

        Assertions.assertEquals(name, role.getName());
    }

    @Test
    void deleteById() {
        roleRepo.deleteById(1L);

        Assertions.assertFalse(roleRepo.exitsById(1L));
    }

    @Test
    void findById() {
        Role role = roleRepo.findById(1L).get();

        Assertions.assertEquals("USER_ROLE", role.getName());
    }

    @Test
    void findAll() {
        List<Role> roles = roleRepo.findAll();

        Assertions.assertEquals(2, roles.size());
        Assertions.assertTrue(roles.contains(new Role(1L, "USER_ROLE")));
        Assertions.assertTrue(roles.contains(new Role(2L, "ADMIN_ROLE")));
    }

    @Test
    void exitsById() {
        Assertions.assertTrue(roleRepo.exitsById(1L));
        Assertions.assertTrue(roleRepo.exitsById(2L));
        Assertions.assertFalse(roleRepo.exitsById(3L));
    }
}