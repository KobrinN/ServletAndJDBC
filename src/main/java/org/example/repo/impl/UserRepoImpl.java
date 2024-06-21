package org.example.repo.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Product;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.UserToProduct;
import org.example.repo.RoleRepo;
import org.example.repo.UserRepo;
import org.example.repo.UserToProductRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepoImpl implements UserRepo {
    private static UserRepo instance;
    private final RoleRepo roleRepo = RoleRepoImpl.getInstance();
    private final UserToProductRepo userToProductRepo = UserToProductRepoImpl.getInstance();
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static final String SAVE_SQL = """
            INSERT INTO public.user (firstname, lastname, role_id)
            VALUES (?, ? ,?) ;
            """;
    private static final String UPDATE_SQL = """
            UPDATE public.user
            SET firstname = ?,
                lastname = ?,
                role_id =?
            WHERE id = ?  ;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM public.user
            WHERE id = ? ;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, firstname, lastname, role_id FROM public.user
            WHERE id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT id, firstname, lastname, role_id FROM public.user;
            """;
    private static final String EXIST_BY_ID_SQL = """
                select EXISTS(SELECT id FROM public.user WHERE id = ?);
            """;

    private UserRepoImpl() {
    }

    public static synchronized UserRepo getInstance() {
        if (instance == null) {
            instance = new UserRepoImpl();
        }
        return instance;
    }

    @Override
    public User save(User user) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            if (!roleRepo.exitsById(user.getRole().getId()))
                throw new SQLException("NO FOUND ROLE WITH ID = " + user.getRole().getId() + "!");
            preparedStatement.setLong(3, user.getRole().getId());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getLong(1),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getRole(),
                        user.getProducts()
                );
            }
            saveProductOfUser(user);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return user;
    }

    private void saveProductOfUser(User user) {
        if (user.getProducts() != null || !user.getProducts().isEmpty()) {
            userToProductRepo.deleteByUserId(user.getId());
            for (int i = 0; i < user.getProducts().size(); i++) {
                userToProductRepo.save(new UserToProduct(null, user.getId(), user.getProducts().get(i).getId()));
            }
        }
    }


    @Override
    public void update(User user) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            userToProductRepo.deleteByUserId(user.getId());
            user.getProducts().forEach(product -> userToProductRepo.save(new UserToProduct(user.getId(), product.getId())));
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            if (!roleRepo.exitsById(user.getRole().getId()))
                throw new SQLException("NO FOUND ROLE WITH ID = " + user.getRole().getId() + "!");
            preparedStatement.setLong(3, user.getRole().getId());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
            saveProductOfUser(user);

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean result;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = createUser(resultSet);
            }


        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findByProductId(Long id) {
        return userToProductRepo.findByProductId(id);
    }

    @Override
    public List<User> findByRoleId(Long id) {
        return findAll().stream()
                .filter(user -> Objects.equals(user.getRole().getId(), id))
                .toList();
    }

    @Override
    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(createUser(resultSet));
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean result = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Role role = roleRepo.findById(resultSet.getLong("role_id")).get();
        List<Product> products = userToProductRepo.findByUserId(id);
        return new User(
                id,
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                role,
                products
        );
    }
}
