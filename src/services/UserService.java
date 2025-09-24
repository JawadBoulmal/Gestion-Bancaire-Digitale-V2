package services;

import enums.Roles;
import interfaces.UserRepository;
import modules.Admin;
import modules.Client;
import modules.Manager;
import modules.User;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository repo){
        userRepository = repo;
    }
    public boolean CreateClient(String firstName , String lastName, String telephone, String CIN, BigDecimal Salaire,String email,String password) throws SQLException {
        UUID userId = UUID.randomUUID();
        User user = new Client(userId,firstName,lastName,telephone,CIN,Salaire,email,password);

        UUID UserID = userRepository.save(user, user.getRole().name());
        if(UserID != null){
            System.out.println("User Has been Created Successfully : \n\tUser ID : "+UserID);
            return true;
        }
        return false;
    }
    public boolean CreateOthers(String firstName , String lastName, String telephone,String CIN, String email,String password) throws SQLException {
        UUID userId = UUID.randomUUID();
        User user = new Manager(userId,firstName,lastName,telephone,CIN,email,password);
        UUID UserID = userRepository.save(user, user.getRole().name());
        if(UserID != null){
            System.out.println("User Has been Created Successfully : \n\tUser ID : "+UserID);
            return true;
        }
        return false;
    }
    public boolean CheckEmailUserIfExists(String Email) throws SQLException {
        return userRepository.emailExist(Email);
    }
}
