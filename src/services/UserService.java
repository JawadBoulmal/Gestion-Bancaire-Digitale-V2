package services;

import enums.Roles;
import interfaces.UserRepository;
import modules.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository repo){
        userRepository = repo;
    }
    public boolean CreateClient(String firstName , String lastName, String telephone, String CIN, BigDecimal Salaire,String email,String password) throws SQLException {
        UUID userId = UUID.randomUUID();
        User user = new Client(userId,firstName,lastName,telephone,CIN,Salaire,email,password);

        UUID UserID = userRepository.save(user, user.getRole());
        if(UserID != null){
            System.out.println("User Has been Created Successfully : \n\tUser ID : "+UserID);
            return true;
        }
        return false;
    }

    public boolean CreateOthers(String firstName , String lastName, String telephone,String CIN, String email,String password , Roles role) throws SQLException {
        UUID userId = UUID.randomUUID();
        User user;
        switch (role){
            case MANAGER -> {
                user = new Manager(userId,firstName,lastName,telephone,CIN,email,password);
            }
            case AUDITOR -> {
                user = new Auditor(userId,firstName,lastName,telephone,CIN,email,password);
            }
            case ADMIN -> {
                user = new Admin(userId,firstName,lastName,telephone,CIN,email,password);
            }
            case TELLER -> {
                user = new Teller(userId,firstName,lastName,telephone,CIN,email,password);
            }
            default -> {
                user = null;
            }
        }
        UUID UserID = userRepository.save(user, role);
        if(UserID != null){
            System.out.println("User Has been Created Successfully : \n\tUser ID : "+UserID);
            return true;
        }
        return false;
    }
    public boolean CheckEmailUserIfExists(String Email) throws SQLException {
        return userRepository.emailExist(Email);
    }

    public ArrayList<User> getAll(){
        return userRepository.getAll();

    }

    public User getUserById(String email, Optional<UUID> id){
        return userRepository.getById(email,id);
    }

    public User updateInformation(User user){
        User result = userRepository.update(user);
        if(result != null){
            System.out.println("User Has been Updated Successfully : \n\tUser ID : "+result.getId());
            return result;
        }
        System.out.println("Failed update the user !");
        return null;
    }
}
