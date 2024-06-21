package org.example.db;

import org.example.exception.DataBaseDriverLoadException;
import org.example.utils.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManagerImpl implements ConnectionManager{
    private static final String DRIVER_CLASS_KEY = "db.driver-class-name";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static ConnectionManager instance;

    private static boolean testConnection = false;
    private static String url = "";
    private static String username = "";
    private static String password = "";
    private Connection connection;
    private ConnectionManagerImpl() {
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManagerImpl();
            loadDriver(PropertiesUtil.getProperties(DRIVER_CLASS_KEY));
        }
        return instance;
    }

    private static void loadDriver(String driverClass) {
        try {
            Class.forName(driverClass);
        } catch (ClassNotFoundException e) {
            throw new DataBaseDriverLoadException("Database driver not loaded.");
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(!testConnection) return DriverManager.getConnection(
                PropertiesUtil.getProperties(URL_KEY),
                PropertiesUtil.getProperties(USERNAME_KEY),
                PropertiesUtil.getProperties(PASSWORD_KEY)
        );
        else return DriverManager.getConnection(url, username, password);
    }

    public static void setTestConnection(String url, String username, String password) throws SQLException {
        ConnectionManagerImpl.testConnection = true;
        ConnectionManagerImpl.url = url;
        ConnectionManagerImpl.username = username;
        ConnectionManagerImpl.password = password;
    }
}
