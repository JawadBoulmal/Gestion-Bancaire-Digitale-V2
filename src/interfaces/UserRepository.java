package interfaces;

import enums.Roles;
import modules.Client;
import modules.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    UUID save(User user, Roles role) throws SQLException;
    User login(String email, String password) throws SQLException ;
    boolean emailExist(String email) throws SQLException;
    boolean existsByTelephone(String telephone);
    ArrayList<User> getAll();
    User update(User user);
    User getById(String email, Optional<UUID> id);
}
