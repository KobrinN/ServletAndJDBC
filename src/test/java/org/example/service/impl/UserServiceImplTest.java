package org.example.service.impl;

import org.example.map.dto.UserDto;
import org.example.map.request.newRequest.NewUserRequest;
import org.example.model.Product;
import org.example.model.Role;
import org.example.model.User;
import org.example.repo.RoleRepo;
import org.example.repo.UserRepo;
import org.example.repo.impl.RoleRepoImpl;
import org.example.repo.impl.UserRepoImpl;
import org.example.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private static UserServiceImpl userService;
    @Mock
    private  UserRepo userRepo;
    @Mock
    private  RoleRepo roleRepo;
    @Mock

    private Role role = new Role(1L, "USER");
    private static UserRepoImpl oldInstance;

    @Before
    public void beforeAll() {
        userRepo = mock(UserRepo.class);
        roleRepo = mock(RoleRepo.class);
        userService = new UserServiceImpl(userRepo, roleRepo);
    }

    @AfterAll
    static void afterAll() {
    }

    @BeforeEach
    void setUp() {
    }
    @Test
    void create() {
        User userReturn = new User(1L, "Oleg", "Olegovich", role);
        when(userRepo.save(any())).thenReturn(userReturn);
        when(roleRepo.findById(any())).thenReturn(Optional.ofNullable(role));
        Long id = userService.create(new NewUserRequest("Oleg", "Olegovich"));
        Assertions.assertEquals(1L, id);
    }

    @Test
    void readAll() {
    }

    @Test
    void readById() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}