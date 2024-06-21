package org.example.service.impl;

import org.example.map.dto.UserDto;
import org.example.map.request.newRequest.NewUserRequest;
import org.example.model.Product;
import org.example.model.Role;
import org.example.model.User;
import org.example.repo.UserRepo;
import org.example.repo.impl.RoleRepoImpl;
import org.example.repo.impl.UserRepoImpl;
import org.example.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static UserService userService;
    private static final UserRepo userRepo = mock(UserRepoImpl.class);
    private static Role role;
    private static UserRepoImpl oldInstance;

    private static void setMock() {
        try(MockedStatic<UserRepoImpl> mockUserRepo = mockStatic(UserRepoImpl.class)){
            mockUserRepo.when(UserRepoImpl::getInstance).thenReturn(userRepo);
            userService = UserServiceImpl.getInstance();
        }
    }

    @BeforeAll
    static void beforeAll() {
    }

    @AfterAll
    static void afterAll() {
    }

    @BeforeEach
    void setUp() {
    }
    @Test
    void create() {
        try(MockedStatic<UserRepoImpl> mockUserRepo = mockStatic(UserRepoImpl.class);
            MockedStatic<RoleRepoImpl> mockRoleRepo = mockStatic(RoleRepoImpl.class)){
            mockUserRepo.when(UserRepoImpl::getInstance).thenReturn(userRepo);
            userService = new UserServiceImpl(userRepo, mockRoleRepo);
            Long id = userService.create(new NewUserRequest("Oleg", "Olegovich"));
            User userReturn = new User(1L, "Oleg", "Olegovich", role);
            when(userRepo.save(any())).thenReturn(userReturn);
            Mockito.doReturn(userReturn).when(userRepo).save(Mockito.any(User.class));
            Assertions.assertEquals(1L, id);
        }

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