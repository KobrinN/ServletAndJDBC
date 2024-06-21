package org.example.repo.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Product;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.UserToProduct;
import org.example.repo.RoleRepo;
import org.example.repo.UserToProductRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserToProductRepoImpl implements UserToProductRepo {
    private static UserToProductRepo instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private final RoleRepo roleRepo = RoleRepoImpl.getInstance();
    private static final String SAVE_SQL = """
            INSERT INTO public.user_product (user_id, product_id)
            VALUES (?, ?);""";
    private static final String UPDATE_SQL = """
            UPDATE public.user_product
            SET user_id = ?,
                product_id = ?
                WHERE id = ?;""";
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM public.user_product
            WHERE id = ?;""";
    private static final String DELETE_BY_USER_SQL = """
            DELETE FROM public.user_product
            WHERE user_id = ?;""";
    private static final String DELETE_BY_PRODUCT_SQL = """
            DELETE FROM public.user_product
            WHERE product_id = ?;""";
    private static final String FIND_BY_ID_SQL = """
            SELECT id, user_id, product_id FROM public.user_product
            WHERE id = ?;""";
    private static final String FIND_ALL_SQL = "SELECT id, user_id, product_id FROM public.user_product;";
    private static final String FIND_PRODUCT_BY_USER_SQL = """
            select p.id, name, quantity, number_of_sold, price from public.product p
            join public.user_product up on p.id = up.product_id
            where up.user_id = ?;""";
    private static final String FIND_USER_BY_PRODUCT_SQL = """
            select u.id, firstname, lastname, role_id from public.user u
            join public.user_product up on u.id = up.user_id
            where up.product_id = ?;""";
    private static final String EXIST_BY_USER_SQL = """
            SELECT EXIST(
                SELECT 1 FROM public.user_product
                WHERE user_id = ?
                LIMIT 1;
            )""";
    private static final String EXIST_BY_PRODUCT_SQL = """
            SELECT EXIST(
                SELECT 1 FROM public.user_product
                WHERE product_id = ?
                LIMIT 1;
            )""";
    private  static final String EXIST_BY_ID_SQL = """
            SELECT exists(
                SELECT 1 FROM public.user_product
                WHERE id = ?
                LIMIT 1;
            );""";
    public static UserToProductRepo getInstance(){
        if (instance == null) instance = new UserToProductRepoImpl();
        return instance;
    }
    private UserToProductRepoImpl(){}
    @Override
    public UserToProduct save(UserToProduct userToProduct) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, userToProduct.getUserId());
            preparedStatement.setLong(2, userToProduct.getProductId());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                userToProduct = new UserToProduct(
                        resultSet.getLong("id"),
                        userToProduct.getUserId(),
                        userToProduct.getProductId()
                );
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return userToProduct;
    }

    @Override
    public void update(UserToProduct userToProduct) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setLong(1, userToProduct.getUserId());
            preparedStatement.setLong(2, userToProduct.getProductId());
            preparedStatement.setLong(3, userToProduct.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private boolean deleteByIdSsl(Long id, String sql){
        boolean result = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    @Override
    public boolean deleteById(Long id) {
        return deleteByIdSsl(id, DELETE_BY_ID_SQL);
    }

    @Override
    public boolean deleteByUserId(Long id){
        return deleteByIdSsl(id, DELETE_BY_USER_SQL);
    }

    @Override
    public boolean deleteByProductId(Long id){
        return deleteByIdSsl(id, DELETE_BY_PRODUCT_SQL);
    }

    @Override
    public Optional<UserToProduct> findById(Long id) {
        UserToProduct userToProduct = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userToProduct = createUserToProduct(resultSet);
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(userToProduct);
    }

    private List<UserToProduct> findByIdSql(Long id, String sql) {
        List<UserToProduct> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(createUserToProduct(resultSet));
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    @Override
    public List<UserToProduct> findAll() {
        List<UserToProduct> result = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(createUserToProduct(resultSet));
            }

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }
    private boolean existByIdSql(Long id, String sql){
        boolean result = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

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
    @Override
    public boolean exitsById(Long id) {
        return existByIdSql(id, EXIST_BY_ID_SQL);
    }

    @Override
    public boolean exitsByUserId(Long id) {
        return existByIdSql(id, EXIST_BY_USER_SQL);
    }

    @Override
    public boolean exitsByProductId(Long id) {
        return existByIdSql(id, EXIST_BY_PRODUCT_SQL);
    }

    private UserToProduct createUserToProduct(ResultSet resultSet) throws SQLException {
        return new UserToProduct(
                resultSet.getLong("id"),
                resultSet.getLong("user_id"),
                resultSet.getLong("product_id")
        );
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Role role = roleRepo.findById(resultSet.getLong("role_id")).orElse(null);
        return new User(
                id,
                resultSet.getString("firstname"),
                resultSet.getString("lastname"),
                role,
                null
        );
    }

    private Product createProduct(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        return new Product(
                id,
                resultSet.getString("name"),
                resultSet.getLong("quantity"),
                resultSet.getLong("number_of_sold"),
                resultSet.getDouble("price"),
                null
        );
    }

    @Override
    public List<User> findByProductId(Long id){
        List<User> users = new ArrayList<>();
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_PRODUCT_SQL)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                users.add(createUser(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return users;
    }

    @Override
    public List<Product> findByUserId(Long id){
        List<Product> products = new ArrayList<>();
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_PRODUCT_BY_USER_SQL)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                products.add(createProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return products;
    }
}
