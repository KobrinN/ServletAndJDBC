package org.example.repo.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Product;
import org.example.model.Role;
import org.example.model.User;
import org.example.model.UserToProduct;
import org.example.repo.ProductRepo;
import org.example.repo.RoleRepo;
import org.example.repo.UserRepo;
import org.example.repo.UserToProductRepo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepoImpl implements ProductRepo {
    private static ProductRepo instance;
    private final UserToProductRepo userToProductRepo = UserToProductRepoImpl.getInstance();
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static final String SAVE_SQL = """
            INSERT INTO public.product (name, quantity, number_of_sold, price)
            VALUES (?, ?, ?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE public.product
            SET
            name = ?,
            quantity = ?,
            number_of_sold = ?,
            price = ?
            where id = ?;""";
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM public.product
            where id = ?;""";
    private static final String FIND_BY_ID_SQL = """
           SELECT id, name, quantity, number_of_sold, price
           FROM public.product
           WHERE id = ?;""";
    private static final String FIND_ALL_SQL = """
            SELECT id, name, quantity, number_of_sold, price
            FROM public.product;""";
    private static final String EXIST_BY_ID_SQL = """
                select EXISTS(SELECT id FROM public.product WHERE id = ?);""";
    private ProductRepoImpl(){}
    public static synchronized ProductRepo getInstance(){
        if(instance == null) instance = new ProductRepoImpl();
        return instance;
    }

    @Override
    public Product save(Product product) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setLong(2, product.getQuantity());
            preparedStatement.setLong(3, product.getNumberOfSold());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                product = new Product(
                        resultSet.getLong(1),
                        product.getName(),
                        product.getQuantity(),
                        product.getNumberOfSold(),
                        product.getPrice(),
                        product.getUsers()
                );
            }
            saveUserOfProduct(product);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return product;
    }

    private void saveUserOfProduct(Product product){
        if(product.getUsers() != null || !product.getUsers().isEmpty()) {
            userToProductRepo.findByProductId(product.getId());
            for (int i = 0; i < product.getUsers().size(); i++) {
                userToProductRepo.save(new UserToProduct(null, product.getUsers().get(i).getId(), product.getId()));
            }
        }
    }

    @Override
    public void update(Product product) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setLong(2, product.getQuantity());
            preparedStatement.setLong(3, product.getNumberOfSold());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setLong(5, product.getId());
            preparedStatement.executeUpdate();
            saveUserOfProduct(product);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean result = false;
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    @Override
    public Optional<Product> findById(Long id) {
        Product product = null;
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                product = createProduct(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> findByUserId(Long id){
        return userToProductRepo.findByUserId(id);
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = createProduct(resultSet);
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }

        return products;
    }

    @Override
    public boolean exitsById(Long id) {
        boolean result = false;
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(EXIST_BY_ID_SQL)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                result = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }


    private Product createProduct(ResultSet resultSet) throws SQLException{
        Long id = resultSet.getLong("id");
        List<User> users = userToProductRepo.findByProductId(id);
        return new Product(
                id,
                resultSet.getString("name"),
                resultSet.getLong("quantity"),
                resultSet.getLong("number_of_sold"),
                resultSet.getDouble("price"),
                users
        );
    }
}
