package com.booktrading.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/book_trading_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&autoReconnect=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        int attempts = 0;
        
        while (attempts < MAX_RETRIES) {
            try {
                Properties props = new Properties();
                props.setProperty("user", DB_USER);
                props.setProperty("password", DB_PASSWORD);
                props.setProperty("autoReconnect", "true");
                props.setProperty("maxReconnects", "3");
                props.setProperty("initialTimeout", "2");
                
                connection = DriverManager.getConnection(DB_URL, props);
                
                if (connection != null && !connection.isClosed()) {
                    return connection;
                }
            } catch (SQLException e) {
                attempts++;
                if (attempts >= MAX_RETRIES) {
                    throw new SQLException("Failed to connect to database after " + MAX_RETRIES + " attempts", e);
                }
                
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Connection retry interrupted", ie);
                }
            }
        }
        
        throw new SQLException("Unable to establish database connection");
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    // For PostgreSQL (alternative configuration)
    public static Connection getPostgreSQLConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/book_trading_system";
        String user = "postgres";
        String password = "password";
        
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found", e);
        }
        
        return DriverManager.getConnection(url, user, password);
    }
}

