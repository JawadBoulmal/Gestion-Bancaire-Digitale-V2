package interfaces;

import enums.Roles;
import modules.Client;
import modules.User;

import java.sql.SQLException;
import java.util.UUID;

public interface UserRepository {
    UUID save(User user, String role) throws SQLException;
    User login(String email, String password) throws SQLException ;
    boolean emailExist(String email) throws SQLException;
    boolean existsByTelephone(String telephone);
}
