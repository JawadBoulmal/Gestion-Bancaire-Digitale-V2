package repositories;

import enums.Roles;
import interfaces.UserRepository;
import modules.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class UserRepositoryImp implements UserRepository {
    private final Connection connection;
    public static User userInfo ;
    public UserRepositoryImp(Connection connection) {
        this.connection = connection;
    }

    public UUID save(User user, String role) throws SQLException {
        String insertUser;

        if(user instanceof Client User){
            insertUser = "INSERT INTO client (id, firstName, lastName, telephone, cin , role, salaire,  email, password) VALUES (?, ?, ?, ?, ?,  ?::roles, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertUser)) {
                stmt.setObject(1, User.getId());
                stmt.setString(2, User.getFirstName());
                stmt.setString(3, User.getLastName());
                stmt.setString(4, User.getTelephone());
                stmt.setString(5, User.getCIN());
                stmt.setString(6, User.getRole());
                stmt.setObject(7, User.getSalaire());
                stmt.setString(8, User.getEmail());
                stmt.setString(9, User.getPassword());
                stmt.executeUpdate();
            }
        }else{
            insertUser = "INSERT INTO user (id, firstName, lastName, telephone, role,  email, password) VALUES (?, ?, ?, ?, ?::roles, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertUser)) {
                stmt.setObject(1, user.getId());
                stmt.setString(2, user.getFirstName());
                stmt.setString(3, user.getLastName());
                stmt.setString(4, user.getTelephone());
                stmt.setString(5, user.getRole());
                stmt.setString(6, user.getEmail());
                stmt.setString(7, user.getPassword());
                stmt.executeUpdate();
            }
        }
        return user.getId();
    }
    @Override
    public User login(String email, String password) throws SQLException {
        String sql = """
                        SELECT u.id, u.firstName, u.lastName, u.telephone, u.role, u.email, 
                               u.password, c.salaire, c.CIN
                        FROM users u
                        LEFT JOIN client c ON u.id = c.id
                        WHERE u.email = ? AND u.password = ?
                    """;
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, password); // In real apps, use hashed passwords!
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            if (Objects.equals(rs.getString("role"), "CLIENT")){
                Client client = new Client(
                        (UUID) rs.getObject("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("telephone"),
                        rs.getString("cin"),
                        rs.getBigDecimal("salaire"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                this.userInfo = client;
                return this.userInfo;
            }else if (Objects.equals(rs.getString("role"), "ADMIN")){
                Admin admin = new Admin(
                        (UUID) rs.getObject("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("telephone"),
                        rs.getString("cin"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                this.userInfo = admin;
                return this.userInfo;
            }else if (Objects.equals(rs.getString("role"), "TELLER")){
                Teller teller = new Teller(
                        (UUID) rs.getObject("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("telephone"),
                        rs.getString("cin"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                this.userInfo = teller;
                return this.userInfo;
            }else if (Objects.equals(rs.getString("role"), "MANAGER")){
                Manager manager = new Manager(
                        (UUID) rs.getObject("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("telephone"),
                        rs.getString("cin"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                this.userInfo = manager;
                return this.userInfo;
            }else if (Objects.equals(rs.getString("role"), "AUDITOR")){
                Auditor auditor = new Auditor(
                        (UUID) rs.getObject("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("telephone"),
                        rs.getString("cin"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                this.userInfo = auditor;
                return this.userInfo;
            }
        }
        return null;
    }

    @Override
    public boolean emailExist(String email) throws SQLException {
        String sql = """
                        SELECT EXISTS (SELECT 1 FROM Users WHERE email = ?)
                    """;
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            boolean exist = rs.getBoolean(1);
            return exist;
        }
        return false;
    }


}
