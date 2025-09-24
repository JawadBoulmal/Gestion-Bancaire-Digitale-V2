package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance;
    private Connection connection = null;

    private static final String URL = "jdbc:postgresql://localhost:5431/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mysecretpassword";

    private Database(){
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connection Successfully!");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("❌ Database connection failed: " + e.getMessage());
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
