package org.example.repo.impl;

import org.example.db.ConnectionManager;
import org.example.db.ConnectionManagerImpl;
import org.example.exception.RepositoryException;
import org.example.model.Role;
import org.example.repo.RoleRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleRepoImpl implements RoleRepo {
    private static RoleRepo instance;
    private final ConnectionManager connectionManager = ConnectionManagerImpl.getInstance();
    private static final String SAVE_SQL = """
            INSERT INTO public.role (name) VALUES (?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE public.role
            SET name = ?
            WHERE id = ?;""";
    private static final String DELETE_SQL = """
            DELETE FROM public.role
            WHERE id = ?;""";
    private static final String FIND_BY_ID_SQL = """
            SELECT id, name FROM public.role
            WHERE id = ?;""";
    private static final String FIND_ALL_SQL = """
            SELECT id, name
            FROM public.role;""";
    private static final String EXIST_BY_ID_SQL = """
            select EXISTS(SELECT id FROM public.role WHERE id = ?);
            """;
    private RoleRepoImpl(){}

    public static synchronized RoleRepo getInstance(){
        if(instance == null) instance = new RoleRepoImpl();
        return instance;
    }
    @Override
    public Role save(Role role) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)){

            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                role = createRole(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return role;
    }

    @Override
    public void update(Role role) {
        try(Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, role.getName());
            preparedStatement.setLong(2, role.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        boolean result;
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)){

            preparedStatement.setLong(1, id);
            result = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return result;
    }

    @Override
    public Optional<Role> findById(Long id) {
        Role role = null;
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)){

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                role = createRole(resultSet);
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return Optional.ofNullable(role);
    }

    @Override
    public List<Role> findAll() {
        List<Role> roles = new ArrayList<>();
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)){

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                roles.add(createRole(resultSet));
            }
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
        return roles;
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

    private Role createRole(ResultSet resultSet) throws SQLException{
        return new Role(
                resultSet.getLong("id"),
                resultSet.getString("name")
        );
    }
}
