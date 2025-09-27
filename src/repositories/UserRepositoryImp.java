package repositories;

import enums.Roles;
import interfaces.UserRepository;
import modules.*;
import org.postgresql.util.PSQLException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImp implements UserRepository {
    private final Connection connection;
    public static User userInfo ;
    public UserRepositoryImp(Connection connection) {
        this.connection = connection;
    }

    public UUID save(User user, Roles role) throws SQLException {
        try{
            String insertUser;
            if(user instanceof Client User){
                insertUser = "INSERT INTO client (id, firstName, lastName, telephone, cin , role, salaire,  email, password) VALUES (?, ?, ?, ?, ?,  ?::roles, ?, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(insertUser)) {
                    stmt.setObject(1, User.getId());
                    stmt.setString(2, User.getFirstName());
                    stmt.setString(3, User.getLastName());
                    stmt.setString(4, User.getTelephone());
                    stmt.setString(5, User.getCIN());
                    stmt.setString(6, User.getRole().name());
                    stmt.setObject(7, User.getSalaire());
                    stmt.setString(8, User.getEmail());
                    stmt.setString(9, User.getPassword());
                    stmt.executeUpdate();
                }
            }else{
                insertUser = "INSERT INTO users (id, firstName, lastName, telephone, role,  email, password) VALUES (?, ?, ?, ?, ?::roles, ?, ?)";
                try (PreparedStatement stmt = connection.prepareStatement(insertUser)) {
                    stmt.setObject(1, user.getId());
                    stmt.setString(2, user.getFirstName());
                    stmt.setString(3, user.getLastName());
                    stmt.setString(4, user.getTelephone());
                    stmt.setString(5, user.getRole().name());
                    stmt.setString(6, user.getEmail());
                    stmt.setString(7, user.getPassword());
                    stmt.executeUpdate();
                }
            }
            return user.getId();
        } catch (Exception e) {
            System.out.println("ERROR"+e.getMessage());
        }
        return null;
    }


    @Override
    public User login(String email, String password) throws SQLException {
        String sql = """
                        SELECT u.id, u.firstName, u.lastName, u.telephone, u.role, u.email, u.password, c.salaire, c.CIN
                        FROM users u
                        LEFT JOIN client c ON u.id = c.id
                        WHERE u.email = ? AND u.password = ?  ;
                    """;
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            if (Objects.equals(rs.getString("role"), "ADMIN")){
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
            }else if (Objects.equals(rs.getString("role"), "CLIENT")){
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

    public boolean existsByTelephone(String telephone) {
        String sql = "SELECT EXISTS (SELECT 1 FROM users WHERE telephone = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, telephone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> getAll() {
        String sql = """
SELECT u.id, u.firstName, u.lastName, u.email, u.telephone, u.role, c.salaire, c.cin ,a.userid , count(a.userid) as account_total
FROM users u
         LEFT JOIN client c ON u.id = c.id
LEFT JOIN accounts a on a.userid = u.id
GROUP BY a.userid, c.cin, c.salaire, u.role, u.telephone, u.email, u.lastName, u.firstName, u.id
ORDER BY account_total DESC
""";
        ArrayList<User> usersList = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            User user = null;
            while (rs.next()) {
                String role = rs.getString("role");
                switch (role){
                    case "CLIENT"->{
                        user = new Client(
                                rs.getObject("id", java.util.UUID.class),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("telephone"),
                                rs.getString("cin"),
                                rs.getBigDecimal("salaire"),
                                rs.getString("email"),
                                rs.getString("account_total")
                        );
                    }
                    case "MANAGER"->{
                        user = new Manager(
                                rs.getObject("id", java.util.UUID.class),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("telephone"),
                                rs.getString("cin"),
                                rs.getString("email"),
                                "Not"
                        );
                    }
                    case "ADMIN"->{
                        user = new Admin(
                                rs.getObject("id", java.util.UUID.class),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("telephone"),
                                rs.getString("cin"),
                                rs.getString("email"),
                                "Not"
                        );
                    }
                    case "AUDITOR"->{
                        user = new Auditor(
                                rs.getObject("id", java.util.UUID.class),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("telephone"),
                                rs.getString("cin"),
                                rs.getString("email"),
                                "Not"
                        );
                    }
                }
                usersList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersList;
    }


    public User getById(String email, Optional<UUID> id){
        String sql = """
               SELECT u.id, u.firstName, u.lastName, u.email, u.telephone, u.role,
                      c.salaire, c.cin
               FROM users u
               LEFT JOIN client c ON u.id = c.id
               WHERE u.email = ? OR u.id = ?
               """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            if (id.isPresent()) {
                stmt.setObject(2, id.get(), java.sql.Types.OTHER);
            } else {
                stmt.setNull(2, java.sql.Types.OTHER);
            }
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role =  rs.getString("role");
                if(Objects.equals(role, "CLIENT")){
                    return new Client(
                            rs.getObject("id", java.util.UUID.class),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getString("telephone"),
                            rs.getString("cin"),
                            rs.getBigDecimal("salaire"),
                            rs.getString("email"),
                            "Not"
                    );
                }else{
                    switch (role){
                        case "MANAGER"->{
                            new Manager(
                                    rs.getObject("id", java.util.UUID.class),
                                    rs.getString("firstname"),
                                    rs.getString("lastname"),
                                    rs.getString("telephone"),
                                    rs.getString("cin"),
                                    rs.getString("email"),
                                    "Not"
                            );
                        }
                        case "ADMIN"->{
                            new Admin(
                                    rs.getObject("id", java.util.UUID.class),
                                    rs.getString("firstname"),
                                    rs.getString("lastname"),
                                    rs.getString("telephone"),
                                    rs.getString("cin"),
                                    rs.getString("email"),
                                    "Not"
                            );
                        }
                        case "AUDITOR"->{
                            new Auditor(
                                    rs.getObject("id", java.util.UUID.class),
                                    rs.getString("firstname"),
                                    rs.getString("lastname"),
                                    rs.getString("telephone"),
                                    rs.getString("cin"),
                                    rs.getString("email"),
                                    "Not"
                            );
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User update(User user){
        String sql = """
                UPDATE users
                SET firstname = ?, lastname = ?, email = ?, telephone = ?, role =  ?::roles , password = ?
                WHERE id = ?
            """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getTelephone());
            stmt.setString(5, user.getRole().name());
            stmt.setString(6, user.getPassword());
            stmt.setObject(7, user.getId(), java.sql.Types.OTHER);
            System.out.println(user.getTelephone());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
